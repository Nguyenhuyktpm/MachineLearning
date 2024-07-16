import os
import pickle
import uuid
import pandas as pd
from flask import Flask, request, jsonify

from sklearn.calibration import LabelEncoder
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import LinearRegression
from sklearn.metrics import f1_score, log_loss, mean_absolute_error, mean_squared_error,r2_score,accuracy_score,precision_score,recall_score
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeRegressor
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
import xgboost as xgb

app = Flask("Training Service")

MODEL_DIR = "E:\\MCLN\\Model"
os.makedirs(MODEL_DIR, exist_ok=True)

@app.route("/")
def hello_world():
  return "Hello world"


def train_regression_model(X_train, y_train, algorithm):
    if algorithm == 'LinearRegression':
        model = LinearRegression()
    elif algorithm == 'RandomForestRegressor':
        model = RandomForestRegressor()
    elif algorithm == 'DecisionTreeRegressor':
        model = DecisionTreeRegressor()
    elif algorithm == 'XGBoostRegressor':
        model = xgb.XGBRegressor()
    else:
        raise ValueError(f"Unsupported algorithm: {algorithm}")

    model.fit(X_train, y_train)
    return model



def train_classification_model(X_train, y_train, algorithm):
    if algorithm == 'LogisticRegression':
        model = LogisticRegression()
    elif algorithm == 'RandomForestClassifier':
        model = RandomForestClassifier()
    elif algorithm == 'DecisionTreeClassifier':
        model = DecisionTreeClassifier()
    elif algorithm == 'XGBoostClassifier':
        model = xgb.XGBClassifier()
    else: 
        raise ValueError(f"Unsupported algorithm: {algorithm}")

    model.fit(X_train, y_train)
    return model

def load_model(model_path):
    with open(model_path, 'rb') as f:
        model = pickle.load(f)
    return model

@app.route("/training_regression", methods=['POST'])
def training_regression():
    train_file_links = request.json.get('train_file_links')
    test_file_link = request.json.get('test_file_link')
    labels_features = request.json.get('labels_features')
    label_target = request.json.get('label_target')
    algorithm = request.json.get('algorithm')

    if not all([train_file_links, test_file_link, labels_features, label_target, algorithm]):
        return jsonify({'error': 'Missing required parameters'}), 400

    try:
        # Đọc và kết hợp dữ liệu từ nhiều file CSV cho tập huấn luyện
        train_data = pd.concat([pd.read_csv(file) for file in train_file_links])
        test_data = pd.read_csv(test_file_link)
    except Exception as e:
        return jsonify({'error': f"Error reading file: {str(e)}"}), 500

    label_encoder = LabelEncoder()
    for col in train_data.columns:
        if train_data[col].dtype == 'object':
            train_data[col] = label_encoder.fit_transform(train_data[col])
    for col in test_data.columns:
        if test_data[col].dtype == 'object':
            test_data[col] = label_encoder.fit_transform(test_data[col])

    if not all(label in train_data.columns for label in labels_features + [label_target]):
        return jsonify({'error': 'One or more specified labels are not in the train dataset columns.'}), 400
    if not all(label in test_data.columns for label in labels_features + [label_target]):
        return jsonify({'error': 'One or more specified labels are not in the test dataset columns.'}), 400

    X_train = train_data[labels_features]
    y_train = train_data[label_target]
    X_test = test_data[labels_features]
    y_test = test_data[label_target]

    try:
        model = train_regression_model(X_train, y_train, algorithm)
        y_test_pred = model.predict(X_test)
        y_train_pred = model.predict(X_train)

        train_loss = mean_squared_error(y_train, y_train_pred)
        test_loss = mean_squared_error(y_test, y_test_pred)
        r2 = r2_score(y_test, y_test_pred)
        mae = mean_absolute_error(y_test, y_test_pred)
        

        # Save the model
        unique_id = uuid.uuid4().hex  # Generate a unique identifier
        model_path = os.path.join(MODEL_DIR, f'regression_model_{unique_id}.pkl')
        with open(model_path, 'wb') as f:
            pickle.dump(model, f)

        response = {
            "train_loss": train_loss,
            "test_loss": test_loss,
            "r2_score": r2,
            "mae":mae,
            "model_path": model_path
        }
    except Exception as e:
        response = {"error": str(e)}

    return jsonify(response)


@app.route("/training_classification", methods=['POST'])
def classification():
    train_file_links = request.json.get('train_file_links')
    test_file_link = request.json.get('test_file_link')
    labels_features = request.json.get('labels_features')
    label_target = request.json.get('label_target')
    algorithm = request.json.get('algorithm')

    if not all([train_file_links, test_file_link, labels_features, label_target, algorithm]):
        return jsonify({'error': 'Missing required parameters'}), 400

    try:
        # Đọc và kết hợp dữ liệu từ nhiều file CSV cho tập huấn luyện
        train_data = pd.concat([pd.read_csv(file) for file in train_file_links])
        test_data = pd.read_csv(test_file_link)
    except Exception as e:
        return jsonify({'error': f"Error reading file: {str(e)}"}), 500

    label_encoder = LabelEncoder()
    for col in train_data.columns:
        if train_data[col].dtype == 'object':
            train_data[col] = label_encoder.fit_transform(train_data[col])
    for col in test_data.columns:
        if test_data[col].dtype == 'object':
            test_data[col] = label_encoder.fit_transform(test_data[col])

    if not all(label in train_data.columns for label in labels_features + [label_target]):
        return jsonify({'error': 'One or more specified labels are not in the train dataset columns.'}), 400
    if not all(label in test_data.columns for label in labels_features + [label_target]):
        return jsonify({'error': 'One or more specified labels are not in the test dataset columns.'}), 400

    X_train = train_data[labels_features]
    y_train = train_data[label_target]
    X_test = test_data[labels_features]
    y_test = test_data[label_target]

    try:
        model = train_classification_model(X_train, y_train, algorithm)
        y_test_pred = model.predict(X_test)
        y_train_pred = model.predict(X_train)

        if hasattr(model, "predict_proba"):
            y_test_proba = model.predict_proba(X_test)
            y_train_proba = model.predict_proba(X_train)
        else:
            y_test_proba = model.decision_function(X_test)
            y_train_proba = model.decision_function(X_train)
        
        train_loss = log_loss(y_train, y_train_proba)
        test_loss = log_loss(y_test, y_test_proba)
        accuracy = accuracy_score(y_test, y_test_pred)
        precision = precision_score(y_test, y_test_pred, average='weighted')
        recall = recall_score(y_test, y_test_pred, average='weighted')
        f1 = f1_score(y_test, y_test_pred, average='weighted')

        # Save the model
        unique_id = uuid.uuid4().hex  # Generate a unique identifier
        model_path = os.path.join(MODEL_DIR, f'classification_model_{unique_id}.pkl')
        with open(model_path, 'wb') as f:
            pickle.dump(model, f)

        response = {
            "train_loss": train_loss,
            "test_loss": test_loss,
            "accuracy": accuracy,
            "precision": precision,
            "recall": recall,
            "f1_score": f1,
            "model_path": model_path
        }
    except Exception as e:
        response = {"error": str(e)}

    return jsonify(response)


@app.route("/use_pretrained_model", methods=['POST'])
def use_pretrained_model():
    model_location = request.json.get('model_location')
    data_file_links = request.json.get('data_file_links')
    labels_features = request.json.get('labels_features')
    label_target = request.json.get('label_target')

    if not all([model_location, data_file_links, labels_features, label_target]):
        return jsonify({'error': 'Missing required parameters'}), 400

    try:
        # Read and concatenate data from multiple CSV files
        data = pd.concat([pd.read_csv(file) for file in data_file_links])
    except Exception as e:
        return jsonify({'error': f"Error reading files: {str(e)}"}), 500

    label_encoder = LabelEncoder()
    for col in data.columns:
        if data[col].dtype == 'object':
            data[col] = label_encoder.fit_transform(data[col])

    if not all(label in data.columns for label in labels_features + [label_target]):
        return jsonify({'error': 'One or more specified labels are not in the dataset columns.'}), 400

    X = data[labels_features]
    y = data[label_target]

    try:
        model = load_model(model_location)
        y_pred = model.predict(X)
        if isinstance(model, (LinearRegression, RandomForestRegressor, DecisionTreeRegressor, xgb.XGBRegressor)):
            loss = mean_squared_error(y, y_pred)
            r2 = r2_score(y, y_pred)
            mae = mean_absolute_error(y, y_pred)
        
            response = {
                "train_loss": loss,
                "r2_score": r2,
                "mae":mae
            }
        elif isinstance(model, (LogisticRegression, RandomForestClassifier, DecisionTreeClassifier, xgb.XGBClassifier)):
            accuracy = accuracy_score(y, y_pred)
            precision = precision_score(y, y_pred, average='weighted')
            recall = recall_score(y, y_pred, average='weighted')
            f1 = f1_score(y, y_pred, average='weighted')

            if hasattr(model, "predict_proba"):
                y_proba = model.predict_proba(X)
            else:
                y_proba = model.decision_function(X)
            
            train_loss = log_loss(y, y_proba)

            response = {
                "train_loss": train_loss,
                "accuracy": accuracy,
                "precision": precision,
                "recall": recall,
                "f1_score": f1
            }
        else:
            return jsonify({'error': 'Unsupported model type'}), 400

    except Exception as e:
        response = {"error": str(e)}

    return jsonify(response)

if __name__ == '__main__':    
  # py -m flask run
  app.run(debug=True) 