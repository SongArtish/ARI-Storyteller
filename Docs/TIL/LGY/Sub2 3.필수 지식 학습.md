# Sub2 3.필수 지식 학습

[TOC]

### Recurrent Neural Networks(RNN)

: 순환 신경망

**이전까지의 신경망(Feed Forward Neural Network)**

- 은닉층에서 활성화 함수(activation function)를 지난 결과값이 출력층 방향으로만 향함

#### RNN

![img](https://wikidocs.net/images/page/22886/rnn_image1_ver2.PNG)

- x: 입력층의 입력 벡터
- y: 출력층의 출력 벡터
- cell: 사이의 은닉층
  - 활성화 함수를 통해 결과를 내보내는 역할을 하는 노드
  - 셀은 이전의 값을 기억(일종의 메모리 역할)하여 과거 상태와의 의존성 제어
  - 메모리 셀, RNN 셀이라고도 함

![img](https://wikidocs.net/images/page/22886/rnn_image2_ver3.PNG)

- **은닉 상태(hidden state)**: 메모리 셀이 출력층 방향으로 or 다음 시점(t+1)의 자신에게 보내는 값
  - t시점의 은닉 상태값 h<sub>t</sub>를 계산하기 위해서는 t-1의 은닉 상태값인 h<sub>t-1</sub>와 t의 입력값을 사용
  -  따라서 이전 시점들에 대해 기억 :arrow_right:현재 시점의 출력에 반영

참고: *https://wikidocs.net/22886*



### Long Short Term Memory(LSTM)

RNN은 이론상 이전의 모든 time step의 상태가 현재 time step t에 영향을 줘야 함.

그러나 데이터의 뒤쪽으로 갈수록 앞쪽의 입력 데이터에 대한 기억을 잃음.

이를 해결하기 위해 몇가지 gate를 추가한 LSTM 등장.

![img](https://t1.daumcdn.net/cfile/tistory/9905CF385BD5F5EC02)

#### LSTM 구조

- 4개의 모듈
- 3개의 게이트
  - 각 게이트는 0이면 정보를 잃고, 1이면 정보를 온전히 기억하게 됨(sigmoid 함수 사용)
  - forget gate(f): 과거 정보를 잊기 위한 게이트
  - input gate(i): 현재 정보를 기억하기 위한 게이트
  - output gate(0): 최종 결과 h를 위한 게이트

- 참고 링크 (https://excelsior-cjh.tistory.com/185)



### Gated Recurrent Unit(GRU)

![A gated recurrent unit neural network.](https://colah.github.io/posts/2015-08-Understanding-LSTMs/img/LSTM3-var-GRU.png)

- LSTM에 비해 게이트의 숫자가 줄어든 GRU
- update gate, reset gate 2가지만 존재

- LSTM과 GRU 둘을 비교해서 사용하는 것이 좋음.
  - LSTM을 사용하여 최적 하이퍼파라미터를 찾았다면 굳이 GRU 사용할 필요 X



### Sequence-to-Sequence(seq2seq)







### Attention Mechanism