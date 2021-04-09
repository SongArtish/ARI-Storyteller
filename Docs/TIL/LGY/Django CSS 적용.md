# Django CSS



## 외부 템플릿 적용

### bootstrap 템플릿

-  참고 링크(https://juni-in.tistory.com/6)

- 사용한 템플릿(https://themewagon.com/themes/free-responsive-bootstrap-4-html5-charity-website-templates-covid/)

1. css를 적용할 template/xxx.html 파일의 최상단에 {% load static %} 작성

![image-20210324115745553](C:\Users\gyuyong\AppData\Roaming\Typora\typora-user-images\image-20210324115745553.png)



2. static 폴더 하위 항목으로 적용할 템플릿 복사

![image-20210324115934418](C:\Users\gyuyong\AppData\Roaming\Typora\typora-user-images\image-20210324115934418.png)



3. static 하위로 넣은 템플릿 파일을 사용할 때는

   ```html
   <link rel="stylesheet" href="{% 'static 하위부터 템플릿파일 상대 경로' %}"
   ```

   로 작성한다.

![image-20210324115951348](C:\Users\gyuyong\AppData\Roaming\Typora\typora-user-images\image-20210324115951348.png)