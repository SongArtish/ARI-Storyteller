# TTS 학습 내용

**Fourier Transform: 임의의 입력 신호를 다양한 주파수를 갖는 주기함수들의 합으로 분해하여 표현**

DFT(Discrete Fourier Transform): discrete한 데이터에 적용하는 푸리에 변환

- signal과 Complex sinusoids의 내적

**FFT(Fast Fourier Transform): 적은 계산량으로 이산 푸리에 변환값을 계산하는 알고리즘**

- time domain에 대한 정보
- 시간에 따라 signal의 주파수가 변화할 때, 언제 주파수가 변하는지 알 수 없음

**STFT(Short-Time Fourier Transform): 시간의 길이를 나눠서 하는 FT**

- 주파수의 특성이 시간에 따라 달라지는 소리 분석에 적합
- 시계열 데이터를 일정한 시간 구간(window size)로 나누어 스펙트럼을 구함



decibel: 10*log_10^[value]

**Mel-scale: Melody scale**(log-scale)

- 사람이 음을 인지하는 기준(threshold)를 반영한 **scale 변환 함수**

- [Hz-음계] 관계가 exponential한 형태로 나타나기 때문에 log 함수를 통과해 mel scale로 표현

- $$
  Mel(f)=2595log(1+f/700)
  $$

- 

**# MFCC: 음성인식 분야의 표준기술**

