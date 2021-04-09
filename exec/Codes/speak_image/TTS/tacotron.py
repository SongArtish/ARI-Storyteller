import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(__file__))+'/TTS/waveglow/')
sys.path.append(os.path.dirname(os.path.dirname(__file__))+'/TTS/')
import numpy as np
import torch

from model import Tacotron2
from data_utils import TacotronSTFT, STFT
from audio_processing import griffin_lim
from train import load_model
from text import text_to_sequence
from denoiser import Denoiser
from scipy.io.wavfile import write
import train
import yaml

import matplotlib
import matplotlib.pylab as plt

matplotlib.use('TkAgg')
def plot_data(data, title, figsize=(16, 4)):
    fig, axes = plt.subplots(1, len(data), figsize= figsize)
    for i in range(len(data)):
        axes[i].imshow(data[i], aspect='auto', origin='lower', interpolation='none')
        axes[i].title.set_text(title[i])
    plt.show()

class TTS_Model:
    
    def __init__(self):
        self.base_dir = os.path.dirname(os.path.dirname(__file__))

        # with open('/home/multicam/samsung_multicam/speak_image/TTS/config.yaml') as f:
        #     self.hparams = yaml.load(f)
        with open('/home/ubuntu/test/backend/Codes/speak_image/TTS/config.yaml') as f:
            self.hparams = yaml.load(f)

        self.load_model()
    
    #Req. 4-1 모델 로드
    def load_model(self):
        
        ####TODO#### 1.학습된 모델 불러오기
        # 학습된 tacotron 모델 주소를 load하고
        # 모델에 hparam과 statedict를 load한다
        checkpoint_path = "/home/ubuntu/test/TTS/checkpoint_28000"
        self.model = train.load_model(self.hparams)
        self.model.load_state_dict(torch.load(checkpoint_path, map_location=torch.device("cpu"))['state_dict'])

        # pass
        

        ####TODO####
        # _ = self.model.cpu().eval().half()
        _ = self.model.cpu().eval()
        
        #waveglow model load
        # waveglow_path = "/home/multicam/checkpoints/waveglow.pt"
        waveglow_path = "/home/ubuntu/test/TTS/waveglow.pt"
        self.waveglow = torch.load(waveglow_path, map_location=torch.device("cpu"))['model']        
        self.waveglow.cpu().eval()
        #self.waveglow.cpu().eval().half()
        for k in self.waveglow.convinv:
            k.float()
        self.denoiser = Denoiser(self.waveglow)

    
    
    def inference(self,text,output_path):
        output_path = output_path 
        
        #Req. 4-2 학습을 마친 이후 test할 수 있는 inference 코드 작성하기
        ####TODO#### 2.tacotron 모델로 mel-spectrogram을 생성 후 waveglow 모델로 waveform을 합성
        # text_to_sequence() 함수를 이용하여 text 전처리   => text_to_sequence(text, ['english_cleaners'])로 sequence 출력              
        # model로 mel_spectrogram예측

        sequence = np.array(text_to_sequence(text, ['english_cleaners']))[None, :]
        # sequence = np.array(text_to_sequence(text, ['basic_cleaners']))[None, :]
        sequence = torch.autograd.Variable(torch.from_numpy(sequence)).cpu().long()
        # pass
        
        ####TODO####
        mel_outputs, mel_outputs_postnet, _, alignments = self.model.inference(sequence)
        title = ["mel_outputs", "mel_outputs_postnet", "alignments"]
        #plot_data((mel_outputs.float().data.cpu().numpy()[0],
        #          mel_outputs_postnet.float().data.cpu().numpy()[0],
        #          alignments.float().data.cpu().numpy()[0].T), title)

        #WaveGlow로 음성 합성하고 Waveform 저장하기
        with torch.no_grad():
            audio = self.waveglow.infer(mel_outputs_postnet, sigma=0.666)
        audio_denoised = self.denoiser(audio, strength=0.01)[:, 0]
                       
        write(output_path, rate=self.hparams['sampling_rate'], data=audio_denoised[0].data.cpu().numpy())
        return output_path
            

    
#Req. 4-2 학습을 마친 이후 test할 수 있는 inference 코드 작성하기    
####TODO####        
if __name__ == '__main__':
    tts_model = TTS_Model()

    text = "Hello everyone, nice to meet you"
    output = tts_model.inference(text, "output.wav")

    print("Success")
    print(output)
    # pass
####TODO####        
