FROM tensorflow/tensorflow:2.2.0rc2-gpu-py3-jupyter

# Install sagemaker-training toolkit that contains the common functionality necessary to create a container compatible with SageMaker and the Python SDK.
RUN pip3 install sagemaker-training

# Copies the training code inside the container
COPY train.py /opt/ml/code/train.py

# Copies the requirements and install them
COPY ./requirements.txt /requirements.txt
RUN pip install -r /requirements.txt

# Defines train.py as script entrypoint
ENV SAGEMAKER_PROGRAM train.py

