import torch
import torch.nn as nn
import numpy as np
import hparams as hp
import os

os.environ["CUDA_DEVICE_ORDER"]="PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"]=hp.train_visible_devices

import argparse
import re
from string import punctuation

from fastspeech2 import FastSpeech2
from vocoder import vocgan_generator

from text import text_to_sequence, sequence_to_text
import utils
import audio as Audio

import codecs
from g2pk import G2p
from jamo import h2j

from synthesize import kor_preprocess, get_Fastspeech2, synthesize

device = torch.device('cpu')

class KOR_Model:
    def __init__(self):
        self.model = get_Fastspeech2(200000).to(device)

        if hp.vocoder == 'vocgan':
            self.vocoder = utils.get_vocgan(ckpt_path=hp.vocoder_pretrained_model_path)
        else:
            self.vocoder = None

    def inference(self, sentence, output_path):
        g2p = G2p()

        text = kor_preprocess(sentence)
        synthesize(self.model, self.vocoder, text, sentence, prefix='step_{}'.format(200000))


if __name__ == '__main__':
    kor_model = KOR_Model()

    sentence ="오늘도 모두가 행복합니다아아아 집이 그립읍니다"
    kor_model.inference(sentence, "temp")


