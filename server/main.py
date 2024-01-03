import requests
from flask import Flask, request, jsonify
import io

import os
import torch
import torchvision
from torch.utils.data import random_split
import torchvision.models as models
import torch.nn as nn
import torch.nn.functional as F
from torchvision.models import ResNet
from PIL import Image
from pathlib import Path
from torchvision.datasets import ImageFolder
import torchvision.transforms as transforms

app = Flask(__name__)

# constants

classes = ["cardboard", "glass", "metal", "paper", "plastic", "trash"]

# defining classes
class ImageClassificationBase(nn.Module):
    def training_step(self, batch):
        images, labels = batch 
        out = self(images)                  # Generate predictions
        loss = F.cross_entropy(out, labels) # Calculate loss
        return loss
    
    def validation_step(self, batch):
        images, labels = batch 
        out = self(images)                    # Generate predictions
        loss = F.cross_entropy(out, labels)   # Calculate loss
        acc = accuracy(out, labels)           # Calculate accuracy
        return {'val_loss': loss.detach(), 'val_acc': acc}
        
    def validation_epoch_end(self, outputs):
        batch_losses = [x['val_loss'] for x in outputs]
        epoch_loss = torch.stack(batch_losses).mean()   # Combine losses
        batch_accs = [x['val_acc'] for x in outputs]
        epoch_acc = torch.stack(batch_accs).mean()      # Combine accuracies
        return {'val_loss': epoch_loss.item(), 'val_acc': epoch_acc.item()}
    
    def epoch_end(self, epoch, result):
        print("Epoch {}: train_loss: {:.4f}, val_loss: {:.4f}, val_acc: {:.4f}".format(
            epoch+1, result['train_loss'], result['val_loss'], result['val_acc']))



class ResNet(ImageClassificationBase):
    def __init__(self):
        super().__init__()
        # Use a pretrained model
        self.network = models.resnet50(pretrained=True)
        # Replace last layer
        num_ftrs = self.network.fc.in_features
        self.network.fc = nn.Linear(num_ftrs, len(classes))
    
    def forward(self, xb):
        return torch.sigmoid(self.network(xb))

# functions

def get_default_device():
    """Pick GPU if available, else CPU"""
    #return torch.device('cpu')
    #if torch.cuda.is_available():
    #    return torch.device('cuda')
    #else:
    return torch.device('cpu')


device = get_default_device()

transformations = transforms.Compose([transforms.Resize((256, 256)), transforms.ToTensor()])

def to_device(data, device):
    """Move tensor(s) to chosen device"""
    if isinstance(data, (list,tuple)):
        return [to_device(x, device) for x in data]
    return data.to(device, non_blocking=True)

def predict_image(img, model):
    # Convert to a batch of 1
    xb = to_device(img.unsqueeze(0), device)
    # Get predictions from model
    yb = model(xb)
    # Pick index with highest probability
    prob, preds  = torch.max(yb, dim=1)
    # Retrieve the class label
    return int(preds[0].item())

def predict_class(img_bytes, model):
    image = Image.open(io.BytesIO(img_bytes))

    transformed_img = transformations(image)
    return predict_image(transformed_img, model)

# variables


print(device)
model = torch.jit.load('model_scripted.pt')
model.cpu()

@app.route('/', methods=["GET", "POST"])
def index():
    if request.method == "POST":
        img_file = request.files.get('img')
        if img_file is None or img_file.filename == "":
            return jsonify({"error": "invalid file"})
        try:
            img_bytes = img_file.read()
            pred = predict_class(img_bytes, model)
            return jsonify({"status": "OK", "error": "none", "prediction": pred})
        except Exception as e:
            return jsonify({"status": "OK", "error": str(e)})
    
    else:
        return jsonify({"status": "OK"})

if __name__ == "__main__":
    app.run()
