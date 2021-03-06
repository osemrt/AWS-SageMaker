from __future__ import print_function

import io
import json
import os
import pickle
import signal
import sys
import traceback

import flask
import pandas as pd

prefix = "/opt/ml/"
model_path = os.path.join(prefix, "model")

# A singleton for holding the model. This simply loads the model and holds it.
# It has a predict function that does a prediction based on the model and the input data.


class ScoringService(object):
    model = None  # Where we keep the model when it's loaded

    @classmethod
    def get_model(cls):
        """Get the model object for this instance, loading it if it's not already loaded."""
        if cls.model == None:
            print(os.path.join(model_path, "model.pkl"))
            cls.model = pickle.load(
                open(os.path.join(model_path, "model.pkl"), 'rb'))

        return cls.model

    @classmethod
    def predict(cls, input):
        """For the input, do the predictions and return them.

        Args:
            input (a pandas dataframe): The data on which to do the predictions. There will be
                one prediction per row in the dataframe"""
        clf = cls.get_model()
        return clf.predict(input)


# The flask app for serving predictions
app = flask.Flask(__name__)


@app.route("/ping", methods=["GET"])
def ping():
    """Determine if the container is working and healthy. In this sample container, we declare
    it healthy if we can load the model successfully."""
    health = ScoringService.get_model() is not None  # You can insert a health check here

    status = 200 if health else 404
    return flask.Response(response="\n", status=status, mimetype="application/json")


@app.route("/invocations", methods=["POST"])
def transformation():
    """Do an inference on a single batch of data. In this sample server, we take data as CSV, convert
    it to a pandas data frame for internal use and then convert the predictions back to CSV (which really
    just means one prediction per line, since there's a single column.
    """
    data = None

    if flask.request.content_type == "application/json":
        # Get input JSON data and convert it to a DF
        input_json = flask.request.get_json()
        input = input_json['data']

        # Predict class using model
        prediction = ScoringService.predict([input])

        # Transform prediction to JSON
        result = {'output': prediction.tolist()[0]}

        resultjson = json.dumps(result)
        return flask.Response(response=resultjson, status=200, mimetype='application/json')
    else:
        return flask.Response(
            response="This predictor only supports JSON data", status=415, mimetype="text/plain"
        )


