import os
import glob
import boto3
import argparse
import tarfile
import pickle
import pandas as pd
import seedir as sd
from sklearn.linear_model import LogisticRegression

prefix = '/opt/ml/'

input_path = prefix + 'input/data'
output_path = os.path.join(prefix, 'output')
model_path = os.path.join(prefix, 'model')
param_path = os.path.join(prefix, 'input/config/hyperparameters.json')

channel_name='training'
training_path = os.path.join(input_path, channel_name)

def read_trainin_data(training_path):
    # Take the set of files and read them all into a single pandas dataframe
    input_files = [ os.path.join(training_path, file) for file in os.listdir(training_path) ]
    if len(input_files) == 0:
        raise ValueError(('There are no files in {}.\n').format(training_path))
    raw_data = [ pd.read_csv(file) for file in input_files ]
    train_data = pd.concat(raw_data)
    return train_data

def prepare_training_data(df):
    X = df.drop(['species'], axis=1)
    y = df['species']
    return X, y

def run_training(args):
    df = read_trainin_data(training_path)
    X,y = prepare_training_data(df)
    model = LogisticRegression()
    model.fit(X, y)
    print(f"train:score={model.score(X,y)}")

    with open(os.path.join(model_path, "model.pkl"), "wb") as file:
        pickle.dump(model, file)
 
if __name__ == '__main__':
    
    parser = argparse.ArgumentParser()
    
    # hyperparameters sent by the client are passed as command-line arguments to the script.
    parser.add_argument('--epochs', type=int, default=50)
    parser.add_argument('--batch-size', type=int, default=64)
    parser.add_argument('--learning-rate', type=float, default=0.05)
    
    args, _ = parser.parse_known_args()
    
    run_training(args)