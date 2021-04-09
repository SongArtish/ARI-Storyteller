# Req. 4-1 Test용 inference 코드 구조 이해

import sys
import os

sys.path.append(os.path.dirname(os.path.dirname(__file__)) + '/VC/')
import torch
import numpy as np
import torch.nn as nn
import torch.nn.functional as F
import yaml
import pickle

from utils2 import *
from functools import reduce
import json
from collections import defaultdict
from torch.utils.data import Dataset
from torch.utils.data import TensorDataset
from torch.utils.data import DataLoader
from argparse import ArgumentParser, Namespace
from scipy.io.wavfile import write
import random
import librosa
from models import AE
from preprocess.audio2mel import Audio2Mel
from preprocess.hyperparams import Hyperparams as hp
from preprocess.utils import melspectrogram2wav


# Req. 4-2 VC_Model() 클래스 구현
class VC_Model(object):

    # Req. 4-3 VC_Model() 클래스 초기화
    ####TODO#### 1.Inferencer() 클래스를 초기화하는 __init__() 함수 구현
    def __init__(self):
        # config.yaml 불러오고 정의
        with open('/home/ubuntu/test/backend/Codes/speak_image/VC/config.yaml') as f:
            self.config = yaml.load(f)

        # build_model()함수 호출하여 모델 정의
        self.build_model()

        # Audio2Mel 인스턴스 정의
        self.fft = Audio2Mel().to(self.get_default_device())

        # MelGAN 보코더 인스턴스 정의
        # self.vocoder = torch.hub.load('seungwonpark/melgan', 'melgan')
        self.vocoder = torch.hub.load('descriptinc/melgan-neurips', 'load_melgan')
        # mel-spectrogram의 평균과 분산 load
        attr_path = '/home/ubuntu/test/VC/attr.pkl'
        with open(attr_path, 'rb') as f:
            self.attr = pickle.load(f)
            ####TODO####

    def get_default_device(self):
        return "cpu"
        #if torch.cuda.is_available():
        #    return "cuda"
        #else:
        #    return "cpu"

    def utt_make_frames(self, x):

        frame_size = self.config['data_loader']['frame_size']
        remains = x.size(0) % frame_size

        if remains != 0:
            x = F.pad(x, (0, remains))
        out = x.view(1, x.size(0) // frame_size, frame_size * x.size(1)).transpose(1, 2)

        return out

    def denormalize(self, x):
        m, s = self.attr['mean'], self.attr['std']
        ret = x * s + m
        return ret

    def normalize(self, x):
        m, s = self.attr['mean'], self.attr['std']
        ret = (x - m) / s
        return ret

    # Req. 4-4 build_model() 함수 구현
    ####TODO#### 2.학습된 모델을 불러오는 build_model() 함수 구현
    def build_model(self):
        # pass
        # 모델 생성하고 config 파일을 load, gpu에 올림
        self.model = cc(AE(self.config)).to(self.get_default_device())
        # 모델을 evaluation 모드로 전환
        # print(self.model)
        self.model.eval()
        # load_state_dict를 이용하여 학습된 VC 모델의 checkpoint load
        # print(f'Load model from /home/ubuntu/test/VC')
        self.model.load_state_dict(torch.load('/home/ubuntu/test/VC/model_140000_2.ckpt', map_location=torch.device("cpu")))

        return

    ####TODO####

    # Req. 4-5 convert_file() 함수 구현
    ####TODO#### 3. waveform을 mel-spectrogram으로 변환하는 convert_file() 함수 구현
    def convert_file(self, path):
        # pass
        # librosa로 samplerate-22050 waveform load
        y, sr = librosa.load(path, sr=hp.sr)
        # print("0",y.shape, np.mean(y)) 
        
        # librosa 라이브러리 내장 함수를 활용하여 일정 데시벨(ex. 15dB) 이하의 값 제거
        y, _ = librosa.effects.trim(y, top_db=hp.top_db)
        # print("0",y.shape, np.mean(y))
        
        # load한 waveform을 torch tensor로 변환, [1,1,x] shape으로 변환
        y = torch.from_numpy(y)
        y = y[None, None]

        # Audio2Mel 클래스를 활용하여 mel spectrogram 생성하고 [x,80] shape로 만들어 준 뒤 np.float32 값으로 변환 후 반환
        mel = self.fft(y.to("cpu"))
        mel = mel.transpose(1,2).detach().cpu().numpy()
        mel = mel[0]
        return mel.astype(np.float32)

    ####TODO####

    # Req. 4-6 melgan() 함수 구현
    ####TODO#### 4. mel-spectrogram을 waveform으로 변환하는 melgan() 함수 구현
    def melgan(self, mel, output_path):
        # pass
        recons = self.vocoder.inverse(mel).squeeze().cpu().numpy()
        # recons = self.vocoder.inference(mel)
        librosa.output.write_wav(output_path, recons, sr=22050)

    ####TODO####

    # Req. 4-7 inference_one_utterance() 함수 구현
    ####TODO#### 5. 두 음성의 mel-spectrogram을 바탕으로 변환된 목소리를 생성하는 inference_one_utterance() 함수 구현
    def inference_one_utterance(self, x, x_cond, output_path):
        # pass
        # self.utt_make_frames() 함수로 두개의 mel-spectrogram을 VC 모델 입력에 맞게 변경
        # print("11", x.shape, x.mean())
        # print("22", x_cond.shape, x_cond.mean())
        x = self.utt_make_frames(x)
        x_cond = self.utt_make_frames(x_cond)
        # print("111", x.shape, x.mean())
        # print("222", x_cond.shape, x_cond.mean())

        # VC 모델의 inference() 함수로 변환된 음성의 mel-spectrogram 생성
        dec = self.model.inference(x, x_cond)

        # 생성된 mel-spectrogram을 [x,80] shape의 numpy ndarray로 변환한 뒤 denormalize() 함수로 전달
        dec = dec.transpose(1, 2).squeeze(0)
        dec = dec.detach().cpu().numpy()
        dec = self.denormalize(dec)
        # print("5", dec.shape, dec.mean())

        # denormalize된 값을 다시 [1,80,x] shape의 torch tensor로 변환한 뒤 melgan() 함수에 값 전달
        dec = torch.from_numpy(dec).unsqueeze(0).transpose(1, 2)
        self.melgan(dec, output_path)

    ####TODO####

    # Req. 4-8 inference() 함수 구현
    ####TODO#### 6. 두 음성의 주소를 받아 inference를 준비하는  inference_from_path() 함수 구현
    def inference(self, src_path, tar_path, output_path):
        # pass
        # self.convert_file() 함수로 두가지 입력 음성을 mel-spectrogram으로 변환
        src_mel = self.convert_file(src_path)
        tar_mel = self.convert_file(tar_path)

        # print("1", src_mel.shape, np.mean(src_mel))
        # print("2", tar_mel.shape, np.mean(tar_mel))

        # self.normalize() 함수로 두 mel-spectrogram을 normalize 한 뒤 torch tensor로 변환
        src_mel = torch.from_numpy(self.normalize(src_mel)).cpu()
        tar_mel = torch.from_numpy(self.normalize(tar_mel)).cpu()
        # print("3", src_mel.shape, src_mel.mean())
        # print("4", tar_mel.shape, tar_mel.mean())

        # self.inference_one_utterance() 함수 호출
        self.inference_one_utterance(src_mel, tar_mel, output_path)

        return output_path
    ####TODO####


##제공##
if __name__ == '__main__':
    src_path = '/home/team1/test/speak_image/VC/input_audio.wav'
    tar_path = '/home/team1/test/speak_image/VC/song0.wav'
    output_path = '/home/team1/test/speak_image/VC/output_0404_2140.wav'

    inferencer = VC_Model()
    output = inferencer.inference("/home/ubuntu/test/0406test.wav","/home/ubuntu/test/backend/Codes/speak_image/VC/song0.wav","/home/ubuntu/test/0406VCtest.wav")
##제공##
