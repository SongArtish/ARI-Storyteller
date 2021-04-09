# VC 학습

## 1. VC의 이해

#### 1.1  VC란

> 소스 음성이 말한 내용을 타겟 음성의 목소리로 변환하여 출력하는 것
>
> 즉 소스에서 언어적인 요소는 남겨두고 타겟에서 추출한 목소리 정보를 입히는 것



#### 1.2 VC 모델

> VAE(Variational Auto Encoder) 구조를 가지며 2개의 인코더와 1개의 디코더를 포함

- 스피커 인코더: 타겟 음성을 통해 타겟의 목소리를 추출

- 컨텐트 인코더: 소스 음성을 통해 소스의 언어적인 정보를 추출

- 디코더: AdaIN(Adaptive Instance Normalization)을 활용하여 소스 음성의 언어적인 정보에 타겟 음성의 음색 정보를 입혀 mel-spectogram을 생성

   - VAE는 대표적인 generative 모델, AdaIN은 컴퓨터 비전 분야에서 image stylization 응용에서 처음 나온 개념

- Discriminative Model vs Generative Model

  - Discriminative Model: 분별모델

    - 입력 X에 대해 출력 Y를 추론하는 방식
    - 데이터 X입력을 통해 출력 Y가 나타날 조건부 확률을 직접 도출하기에 지도학습에 해당

  - Generative Model: 생성모델

    - 분포와 샘플

      > 정규 분포는 평균과 분산을 파라미터로 지님. 이를 통해 데이터를 샘플링


    - 영상이나 음성과 같은 고차원 데이터, 비정형 데이터의 분포를 나타내는 것은 복잡해 정규분포와 같은 식으로 표현할 수 없음

      > 수식을 모르기 때문에 샘플링을 통해 랜덤 데이터를 생성하는 것이 거의 불가능

    - 이를 해결하기 위해 생성모델은 학습 데이터셋을 통해 수식 대신 모델을 이용하여 데이터셋의 분포를 근사하고 샘플링

      > 학습된 정보를 바탕으로 학습 데이터의 분포를 따르는 유사한 데이터 생성이 가능

    - 생성 모델은 데이터의 참 분포를 주어진 데이터셋으로부터 근사

      > AE는 라벨링 되지 않은 데이터를 저 차원의 특징 코드로 압축

    - VAE와 AE는 구조적으로 유사하나  VAE는 잠재코드의 분포가 정규 분포를 따르도록 한 모델



#### 1.3 보코더

- 보코더의 필요성

  > 딥러닝으로 오디오 신호를 처리하고자할 때 스펙토그램을 특징으로 입력받는다. 
  >
  > 스펙토그램은 크기와 위상으로 나뉘는데 위상은 허수부이기에 입력이 불가능하다. 
  >
  > 따라서 딥러닝의 입력과 출력은 스펙토그램의 크기가 되고 위상 없이 복원하기 위해 보코더가 필요하다.

- 보코더의 종류
  - griffin-lim
  - wavenet
    - 엄밀히 따지면 보코더는 아니지만 local condition을 추가하여 보코더로 사용가능
    - 성능은 좋지만 학습이 느림
  - waveglow
    - wavenet + glow model로 wavenet보다 좋은 퍼포먼스를 보임
    - 개선된 보코더지만 여전히 느림
  - melgan
    - 압도적으로 빠른 속도를 보여줌





## 2. 데이터 전처리

#### 2.1 데이터셋

- VCTK Corpus 데이터셋의 사용
  - 109명의 음성으로 구성된 데이터셋, 텍스트가 존재하지 않음
  - TTS에서는 단일 여성화자 데이터셋인 LJ Speech셋을 사용 (VC로는 부적합
- 데이터셋 처리 프로세스
  - 데이터셋의 다운로드 : [VCTK Corpus](https://send-anywhere.com/web/downloads/F3REUH6S)
  - train, validation, test로 분리하기
  - VC모델의 학습을 최대화 하기 위해 데이터셋을 더 작은 음성 조각으로 분리하기
  - 학습이 가능하도록 mel-spectogram으로 변환시키기

#### 2.2 전처리 실행하기

- 전처리 실행 명령어

  - ./speak_image/VC/preprocess 에서 ./preprocess_vctk.sh로 실행하기

    - sh: shell script 파일로 실행 명령어를 모아놓은 파일

    - 전처리에 필요한 과정을 순서대로 실행

      0. load vctk.config

      1. execute make_datasets_vctk.py
      2. execute reduce_dataset.py
      3. execute sample_single_segments.py for train
      4. execute sample_single_segments.py for in_test
      5. execute sample_single_segments.py for out_test

- make_datasets_vctk.py 의 주요 프로세스

  - 데이터셋 로드
  - 데이터셋 역할에 맞게 분할
  - 전처리 시작
    - 데이터를 mel-spectogram으로 변환
    - mel-spectogram에 대한 평균과 분산 계산
    - 도출된 평균과 분산에 대해 normalize 과정
    - 해당 과정이 끝난 후 각각 역할에 맞는 pkl(pickle) 파일로 저장
  - 명세서상 구현해야하는 파일

- reduce_dataset.py의 역할

  - 특정 길이보다 짧은 음성은 학습을 위해 데이터셋에서 제거하는 과정

- sample_single_segment.py

  - 고정된 길이의 음성 조각을 샘플해서 최종적으로 데이터셋을 저장



## 3. 학습하기

#### 3.1 model.py의 이해

- AE (Auto Encoder) 클래스
  - 최종적으로 VC모델은 AE클래스로 구현
  - AE 클래스는 2개의 인코더와 1개의 디코더의 결합으로 구현
  - SpeakerEncoder로부터 음성 정보를 추출
  - ContentEncoder로부터 언어적인 정보를 추출
  - Decoder에서는 2개의 인코더로 추출된 정보를 통해 변환된 음성을 추출

#### 3.2 solver.py

- VC학습을 위해 AE 모델을 학습시키는 코드를 작성하는 파일
- 명세서에서 작성해야하는 부분
- 여타 다른 학습과 마찬가지로 dataset을 로드해서 학습시키면서 일정 iteration마다 중간 학습된 모델을 저장하는 기능을 수행



#### 3.3 고찰

- batch_size

  - 한번에 처리하는 크기

  - 실행 명령어에서 iteration크기를 명시해주었기에 VC학습에서는 batch_size에 의해 iteration의 크기가 달라지지 않고 batch_size의 크기와 학습 시간이 비례한다.

  - 생성 모델로 학습하다보니 loss 값을 계산할 때 Kullback-Leibler Divergence(KL divergence)를 사용하기에 loss값을 2가지로 추출 

    > 추가 학습 필요

- melgan

  - 여러층의 conv1d으로 이루어진 모델

  - 각 층마다 Upsampling과 Residual stack으로 구성

  - Residual stack내부에는 conv1d가 있고 residual connection을 3버 거친다.

  - 이때 입력 텐서의 크기는 (batch_size, 80, frame_length)이다.

    :star:이때 현재 생성된 모델의 configure 파일을 보면 input과 output의 채널 사이즈가 512로 구성되었는데 inference에서 melgan을 통해 보코더 과정을 거칠 때 정상 작동할지가 의문

  - 출력은 오디오 신호로 크기는 (batch_size, 1, frame_size*hop_size)

-  추가 목표

  - 실제 inference과정에서 입력과 출력과정을 살펴보고 실제로 잘 진행되는지 확인
  - 각 알고리즘의 동작방식은 다 보지 못해도 역할에 대해서 파악하기
  - 학습이 잘 진행되면 한국어 TTS에 적용되는지 확인하기

