# AR

[TOC]



## AR 구현 방식

### 센서 기반

gps 좌표, 가속도계, 자기장계와 같은 **센서**를 이용하여 AR을 구현하는 방식

ex) 포켓몬go

장점

- 상대적으로 적은 연산량으로 구현 가능

단점

- 센서 성능에 따라 정확도가 달라짐

- 바닥 인식이 없으므로 3d 모델을 바닥/벽면 기준으로 그릴 수 없으므로 제한적인 어플리케이션만 생성할 수 있음

라이브러리

- GeoAR.js: gps 정보를 이용하여 위치 기반 AR 기능 제공. ar.js에 포함되어있음.



### 비전 기반

### 마커 기반

#### fiducial

일정한 패턴을 가진 이미지를 이용해 공간을 인식하는 방식

장점

- 정확도가 높음

- 환경변화에 영향을 크게 받지 않음

단점

- 사용자가 마커를 준비해야만 사용할 수 있으므로 제한적 사용

#### natural feature tracking

2D 이미지(ex. 사진)이나 3D 오브젝트(ex. 사람 얼굴)를 인식하여 pose estimation을 수행

장점

- fiducial 방식보다 더 다양한 종류의 AR 어플리케이션에 적용 가능함

단점

- 더 높은 인식 능력 필요

- 상대적으로 더 많은 연산량 소요

### 마커리스(markerless) 기반

주변 환경에 대한 정보가 없는 상태에서 센서의 정보를 이용하거나 환경 정보(ex. 벽, 바닥)를 인식하여 기기의 위치를 추적하는 방식

SLAM(Simultaneous Localization and Mapping)이라는 기법을 이용하여 이미지를 분석

1개의 카메라를 이용하여 수행할 경우 Monocular Visual SLAM이라고 부름. 모바일 기기가 이에 해당

장점

- 어플리케이션 제작에 제한이 적음

단점

- 연산량이 많음 :arrow_right: 모바일 기기에서는 다른 센서 정보를 함께 사용하는 hybrid 방식을 주로 이용

예시

- 8th Wall
  - 자체적으로 SLAM 엔진 구현해서 사용

- ARCore
  - google에서 android용으로 제작한 AR 프레임워크

- ARKit
  - apple에서 ios 기반 기기를 위해 제작



## Web에서의 AR 적용

### ARCore

특징

- SLAM + 센서를 결합한 hybrid 방식

- pose detection 수행

- 기기의 이동, 회전을 포함하는 6DoF(Degree of Freedom) 모션 추적 가능

대표 기능

- hit-test

  - 기기의 카메라에서 광선을 뻗어 SLAM을 통해 인식된 주변 환경과의 충돌 지점을 구하는 기능
  - 바닥, 벽, 천장과 같은 주변 환경을 인식하고 그 위에 3D 오브젝트를 표시하는 등의 방식으로 활용할 수 있음. ex) IKEA의 가구 배치 AR

  ![2f7b8cc5a8fb9b35.jpeg](https://d2.naver.com/content/images/2020/10/0a7056be-741f-108d-8174-a0d67b7b0308.jpg)

  *WebXR hit-test*

- dom-overlay

  - WebGL 레이어 위에 DOM 엘리먼트를 렌더링하는 기능
    - AR 세션의 gui를 WebGL 없이 css와 js 이벤트 핸들러를 이용하여 간단하게 구현 가능
    - CSS3의 3D transform을 이용하여 다양한 엘리먼트(ex. 비디오, 이미지)를 실제 환경 위에 표시할 수 있음



### WebXR

WebXR Device API

"X"는 변수 x를 의미하며 V 또는 A를 대입하면 WebVR이나 WebAR이 되는데 둘 다 수행이 가능하다는 뜻

#### 특징

기존 WebVR API를 **deprecated**시키고, AR 기능을 포함시키는 형태로 확장하여 구현됨

- deprecated: 기존 함수/클래스가 필요없어져서 삭제하려할 때, 다른 곳에서 사용 중이라 바로 삭제하기 까다로운 경우에 사용. 바로 삭제하지 않고 다른 곳에서 사용하지 않도록 유도한 후(=deprecated), 사용하는 곳이 없어지면 삭제함.

현재 AR 기능 지원하는 기기가 한정적임

- Android
  - Chrome 브라우저만 지원
    - 향후 Chromium 기반 브라우저에서 AR기능까지 적용 가능할 것으로 기대.
    - ex) 삼성 인터넷. 현재는 VR 세션만 지원 중이지만 AR 세션에 대한 추가 지원 예정
  - ARCore 설치 가능한 기기만 지원
    - WebXR Device API가 ARCore의 기능을 활용하는 방식이기 때문
    - 안정적인 품질의 AR 경험을 위한 제한
    - 센서, 카메라, cpu의 성능이 조건을 만족해야 함
    - ARCore가 기기에 설치되어 있지 않으면 설치 페이지로 넘어감

- iOS
  - Firefox Reality 브라우저만 기능

#### fallbacks & considerations

- fallback: 어떤 기능이 약해지거나 제대로 동작하지 않을 때, 이에 대처하는 기능 또는 동작

ScenViewer

- google 앱에 내장된 3d 뷰어
- 브라우저와 무관하게 ARCore가 설치 가능한 Android를 모두 지원

AR Quick Look

- 애플에서 제공
- iOS 11 이상의 기기 모두 지원

상세한 지원 방식 및 추가 정보는 링크(https://d2.naver.com/helloworld/0527763) 확인

#### 구현

https://d2.naver.com/helloworld/0189619 참고



