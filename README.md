# 아리 구현동화 (Ari Storytelling)

>2021년 SSAFY 2학기 특화프로젝트
>
>- [소개영상](https://youtu.be/udTZicdGTxs)

---

[TOC]

---

![Ari Storytelling](Docs/img/index.png)



## Intro

- 개발기간: 2021년 3월 8일 - 2021년 4얼 9일
- 프로젝트 배경
  - 아이들에게 동화책을 읽어주기 어려운 부모들의 문제 해결
  - 아이들의 독서 습관 독려
- 서비스 개요
  - **어린이를 위해 그림동화책을 읽어주는 기능**을 제공한다.
  - **Image Captioning**: 이미지 캡셔닝을 통해 그림통화책의 내용을 텍스트로 전달한다.
  - **Text-to-speech(TTS)**: 텍스트를 음성으로 재생할 수 있다.
  - **Voice Conversion**: 부모님 등 제3자의 목소리로 그림동화책을 들을 수 있다.
  - **증강현실(AR)**: 그림책에 추가된 AR 기능을 사용할 수 있다.
- 프로젝트명
  - **아리(ARI) 구연동화**
  - AI + AR 기술이 합성된 것을 상징적으로 표현
  - Ari를 지혜를 상징하는 부엉이 모습으로 캐릭터화
- 소개영상
  - https://youtu.be/udTZicdGTxs


## :computer: Tech Stack

- Frontend
  - Django
  - HTML, CSS
  - Bootstrap
- Backend
  - Django
  - Android



## :gear: Install and Usage

> `git clone`을 한 후 `backend` 폴더에서 **반드시** 아래의 환경설정을 해주어야 한다.

### 1. Python 버전

**python==3.7**

- AI 패키지 연결을 위해서 python==3.7 환경에서 세팅이 되어야한다.

### 2. pip 설치

- 먼저 requirements.txt에 있는 python package를 설치한다.

```bash
$ pip install -r requirements.txt
```

### 3. Image Captioning을 위한 설정

**3.1 CUDA 드라이버 설치**

- cmd에서 아래의 명령어로 현재 컴퓨터 사양에 맞는 CUDA Version을 확인한다.

```
nvidia-smi
```

- 여기서는 CUDA Version: 10.2를 기준으로 한다.
- CUDA 드라이브 설치 페이지에 가서 버전에 맞는 드라이브를 설치한다.
- 드라이브 설치 후 버전에 맞는 cudatoolkit과 pytorch를 다시 설치해준다.
- 먼저 기존에 설치한 pytorch를 제거해준다.

```
conda remove pytorch
```

- 그리고 다시 설치해준다.

```
conda install pytorch=1.7.0 torchvision torchaudio cudatoolkit=10.1 -c pytorch
```

**3.2 pdy 파일 생성**

- `backend/Codes/speack_image/IC/vqa_origin/` 폴더 위치에서 아래의 명령어를 실행한다.

```bash
$ python setup.py build develop
```

- `backend/Codes/speack_image/IC/vqa_origin/maskrcnn_benchmark/` 폴더에 __C로 시작하는 .pyd 파일이 생성된 것을 확인한다.
- :ballot_box_with_check: 작업환경이 변경되면 반드시 pdy 파일을 다시 생성해주어야 한다!!!

**3.3 checkpoints 다운로드**

- 구글 공유폴더에 들어있는 checkpoints 폴더를 다운받는다. (학습된 데이터)

- 압축을 풀고 폴더 내의 파일&폴더를 `backend\Codes\speak_image\IC` 옮겨준다.

- **checkpoints 폴더 위치 설정**

  > Books 앱의 views.py의 Speak_Image.speak_image() 메소드 실행을 위한 폴더 위치 설정.
  >
  > 설정 안할 시 에러 날 수 있음.

  - 우리프로젝트 최상위 폴더(디렉토리)\backend\Codes 경로에 speak_image와 web 디렉토리와 동일한 레벨에 저장해두세요.
  - 다운로드 경로: [규용이 구글드라이브](https://drive.google.com/drive/folders/1_X6ySvYQPWw9MSHc9xnqGRsA2F38D9NP)
  - checkpoints 다운로드

### 4. 서버실행

- 위의 모든 설정이 끝나고나면, `backend` 폴더에서 아래의 명령어로 서버를 실행한다.

```bash
$ python manage.py runserver
```

- 서버를 실행 후 **안드로이드에서 접속**해준다.



## :family_man_woman_boy:Team Member

### 이규용(Lee Gyuyong)

> [@github](https://github.com/gyuyong290)

- Team Leader
- Frontend

### 윤서완(Yoon Seowan)

- Android

### 이송영(Lee Songyoung)

- Backend
- UCC

### 이형창(Lee HyungChang)

- Backend
- DataBase

### 조성훈(Cho Sunghoon)

- AI
- Server



## 알려진 버그

> 2021년 4월 9일 현재까지 없음



***Copyright\* © 2021 SSAFY_SEOUL4_TEAM2_ARI**

