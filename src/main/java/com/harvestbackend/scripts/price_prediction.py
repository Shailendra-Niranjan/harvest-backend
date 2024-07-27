import os
import sys
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.preprocessing.sequence import TimeseriesGenerator
from keras.models import Sequential
from keras.models import load_model
from keras.layers import Dense, LSTM, Dropout

dataset_path_list = os.listdir('./dataset/')
dataset_list = []
N_INPUTS = 3
N_FEATURES = 1

for i in dataset_path_list:
    ds = pd.read_csv(os.path.join('./dataset/', i), usecols=[0, 1], index_col='Month', parse_dates=True)
    dataset_list.append(ds)


def preprocessing(train_dataset, test_dataset):
    scaler = MinMaxScaler(feature_range=(0, 1))
    scaler.fit(train_dataset)
    scaled_train = scaler.transform(train_dataset)
    scaled_test = scaler.transform(test_dataset)
    return scaled_train, scaled_test


def inverse_transform(train_dataset, prediction):
    scaler = MinMaxScaler(feature_range=(0, 1))
    scaler.fit(train_dataset)
    inv_pred = scaler.inverse_transform(prediction)
    return inv_pred


def get_prediction(model, dataset):
    last_train_batch = dataset[-N_INPUTS:]
    last_train_batch = last_train_batch.reshape((1, N_INPUTS, N_FEATURES))
    prediction = model.predict(last_train_batch)[0]
    return [prediction]


def generator(scaled_dataset):
    x_generator = TimeseriesGenerator(scaled_dataset, scaled_dataset, length=N_INPUTS, batch_size=N_FEATURES)
    return x_generator


def create_model():
    model = Sequential()
    model.add(LSTM(units=50, return_sequences=True, input_shape=(N_INPUTS, N_FEATURES)))
    model.add(Dropout(0.2))
    model.add(LSTM(units=50, return_sequences=True))
    model.add(Dropout(0.2))
    model.add(LSTM(units=50, return_sequences=True))
    model.add(Dropout(0.2))
    model.add(LSTM(units=50))
    model.add(Dropout(0.2))
    model.add(Dense(units=1))
    model.compile(optimizer='adam', loss='mean_squared_error')
    return model


def test_pred(model, scaled_train, test_len):
    test_predictions = []
    first_eval_batch = scaled_train[-N_INPUTS:]
    current_batch = first_eval_batch.reshape((1, N_INPUTS, N_FEATURES))
    for j in range(test_len):
        current_pred = model.predict(current_batch)[0]
        test_predictions.append(current_pred)
        current_batch = np.append(current_batch[:, 1:, :], [[current_pred]], axis=1)
    return test_predictions


def train_model(scaled_train, idx, epochs=50):
    train_generator = generator(scaled_train)
    model = create_model()
    model.fit(train_generator, epochs=epochs)
    model.save(f'./model/model{idx}.keras')
    return model


def get_prediction_from_selected_model(dataset_index):
    df = dataset_list[dataset_index]
    _len = len(df)
    _train = df.iloc[:_len-N_INPUTS]
    _test = df.iloc[_len-N_INPUTS:]
    _scaled_train, _scaled_test = preprocessing(_train, _test)
    model = load_model(f'./model/model{dataset_index}.keras')
    _prediction = get_prediction(model, _scaled_test)
    inv_pred = inverse_transform(_train, _prediction)
    return round(inv_pred[0][0], 2)


def train_all(idx):
    n_ds = dataset_list[idx]
    ds_len = len(n_ds)
    train = n_ds[:ds_len - N_INPUTS]
    test = n_ds[ds_len - N_INPUTS:]
    n_scaled_train, n_scaled_test = preprocessing(train, test)
    train_model(n_scaled_train, epochs=100, idx=idx)
    print(f'Trained model for {dataset_path_list[idx]}')


if __name__ == '__main__':
    # for i in range(len(dataset_list)):
    #     train_all(i)
    n_idx = int(sys.argv[1])
    pred = get_prediction_from_selected_model(n_idx)
    print(pred)










