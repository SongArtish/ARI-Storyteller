# GAN의 이해

## 사전 학습

### 분별모델(Discriminative Model)

입력 x가 있을 때 y를 직접 추론하는 방법.

레이블링이 필요. 지도학습.

데이터를 구분하는 경계(Decision boundary)를 찾는 것을 목표로 함

### 분포

평균, 분산을 파라미터로 갖는 수식으로 표현

데이터의 "분포"를 알고있으면 데이터를 샘플링할 수 있음

- ex) 정규분포에 따라  랜덤 숫자 생성 가능

but 영상/음성은 고차원 데이터. 이런 고차원 데이터를 분포로 나타내는 것은 너무 복잡

따라서 수식을 구할 수 없고, 샘플링을 통한 랜덤 데이터를 생성하는 것도 거의 불가능



![img](https://blog.kakaocdn.net/dn/bksZCn/btqFljt1Sw1/HKbd73ArhIt3oAYSY09K70/img.png)

### 생성모델(Generative Model)

: 수식 대신 모델(ex. 인공신경망)을 이용한 데이터셋의 분포를 근사하고 샘플링 가능한 모델을 학습하는 것

학습 데이터의 분포를 따르는 유사한 데이터 생성이 가능한 모델

참 분포를 몇 가지 가정을 통해 수식으로 유도 -> 생성 모델 -> Explicit density 모델로 분류

참 분포에 대한 유도 없이 생성된 모델 -> Implicit density 모델로 분류



## GAN(Generative Adversarial Model)

### 개념

샘플 생성하는 문제를 Min-Max 게임 형태로 접근 -> 기존 비지도 학습에서 데이터의 확률분포를 모델링하는데 생기던 문제 해결함

새로운 데이터를 생성하는 생성자 vs 데이터를 분별하는 구별자 ex) 위조지폐범 vs 경찰

각자 성능을 높이는 것을 목표로 하고 경쟁 학습 함 -> 생성자가 진짜 같은 가짜 이미지 만들어 냄

![img](https://blog.kakaocdn.net/dn/MZPlV/btqFbCeExIL/HqnvkylX6va0S4YwWYR4f1/img.png)

위조지폐와 경찰을 예로 설명한 GAN 모델 (Goodfellow, lan el al., 2014)

![img](https://t1.daumcdn.net/cfile/tistory/99A0C1335B758B401B)

[출처 : https://hyeongminlee.github.io/post/gan001_gan/]



### 이론적 확인

1. Generator는 기존 샘플(training, real) 분포를 파악하여 새로운 샘플(fake)을 생성함
2. Discriminator는 샘플이 Generator 또는 Training 중 어디에서 온건지 확률을 평가함 (Minimax tow-player game)
3. Genertaor가 Discriminator 분포를 완벽한 수준으로 복원하면 Discriminator가 Generator의 산출물(fake)와 Training(real)을 구분할 확률은 1/2가 됨

![img](https://blog.kakaocdn.net/dn/brTEsR/btqEFJmgk35/rebN81BICBRvClFs2sCbOk/img.png)

GAN 학습 과정 (출처 : Generative Adversarial Nets Goodfellow, Ian et al. 2014)

- Q_model(x|z) : 정의하고자 하는 z값을 줬을 때 x 이미지를 내보내는 모델
- P_data(x) : x라는 data distribution은 있지만 어떻게 생긴지는 모르므로, P 모델을 Q 모델에 가깝게 가도록 함
- **파란 점선 ---** : discriminator distribution (분류 분포) > 학습을 반복하다보면 가장 구분하기 어려운 구별 확률인 1/2 상태가 됨
- **녹색 선 ⎻** : generative distribution (가짜 데이터 분포)
- **검은색 점선 ---** : data generating distribution (실제 데이터 분포)



### 한계점

1. 안정성이 떨어져서 학습이 어려움
2. 같은 영상 한두 개만 반복 생성하는 모드 붕괴(mode collapsing) 발생 가능
   - 학습시키려는 모형이 실제 데이터의 분포를 커버하지 못하고 다양성을 잃어버리기 때문
3. 최적점(optimal point)에 도달했는지 판단하기 어려움.(=수렴 판별이 어려움)



### 발전

GAN으로부터 파생된 다양한 GAN

DCGAN, StarGAN, cGAN, WGAN, EBGAN, BEGAN, SRGAN, SEGAN, StyleGAN 등



#참고링크

- https://wegonnamakeit.tistory.com/54



## MelGAN 보코더

MelGAN: Generative Adversarial Networks for Conditional Waveform Synthesis

![image-20210326001110930](C:\Users\gyuyong\AppData\Roaming\Typora\typora-user-images\image-20210326001110930.png)

We plug the MelGAN model in an end-to-end speech synthesis pipeline (Figure 2) and evaluate the text-to-speech sample quality with competing models.

The results indicate that MelGAN is comparable to some of the best performing models to date as a vocoder component of TTS pipeline

| Model                  | MOS  | 95% CI |
| ---------------------- | ---- | ------ |
| Tacotron2 + WaveGlow   | 3.52 | ±0.04  |
| Text2mel + WaveGlow    | 4.10 | ±0.03  |
| Text2mel + MelGAN      | 3.72 | ±0.04  |
| Text2mel + Griffin-Lim | 1.43 | ±0.04  |
| Original               | 4.46 | ± 0.04 |

Table 5: Mean Opinion Score of end to end speech synthesis. Evaluation protocol/details can be found in appendix B.



![image-20210326001624917](C:\Users\gyuyong\AppData\Roaming\Typora\typora-user-images\image-20210326001624917.png)



Modelling raw audio is a particularly challenging problem because of the high temporal resolution of the data (usually atleast 16,000 samples per second) and the presence of structure at different timescales with short and long-term dependencies. Thus, instead of modelling the raw temporal audio directly, most approaches simplify the problem by modelling a lower-resolution representation that can be efficiently computed from the raw temporal signal.

 Audio modelling is thus typically decomposed into two stages. The first models the intermediate representation given text as input. The second transforms the intermediate representation back to audio. In this work, we focus on the latter stage, and choose mel-spectogram as the intermediate representation