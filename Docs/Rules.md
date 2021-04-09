# Rules

> 프로젝트에 앞서 간단한 규칙을 정하였다.

2021.03.09

---

[TOC]

---



## 약속🤞🏼

- 기획기간 동안은 매시 50분에 10분간 휴식!



## Git Convention 👣

### Commit Message

- Message Type: `English` `PascalCase`로 작성!
- 예시

```markdown
Docs: 데이터베이스 작성 #S04P12A402-2

Feat: 모바일반응형(회원가입/로그인/아이디비밀번호찾기) #S04P13A402-52

Refactor: 같은 동네 처리부분 db수정 후 조회부분 코드 수정 #S04P13A402-66
```

### **Branch Rule**

- 단위 기능 1개 완료 후, MR 날리기!
- 예시

```markdown
BE/feature/board
```



## Coding Style & Naming Rule 👣

### Django

- 파일명: `snake_case`

- 모델 클래스명(

  Models.py

  , 

  serializers.py

  ): 

  ```
  PascalCase
  ```

  - 클래스 내부 변수(속성값, field, column): snake_case

- 함수명(

  Views.py

  ): 

  ```
  snake_case
  ```

  - 함수 내부 일반 선언: `snake_case`
  - ex) like_movies = serializer.data.get('like_movies'), user = User()