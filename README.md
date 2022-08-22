### 원티드 클론 코딩 - 라이징테스트 8th  / 원티드 A - Server

#### [22/08/20 - 22/09/02] 2주간 진행
 
#### [sua/김민서] [화니/김영환]

## 2022.08.20
#### 0. 기획서 작성
#### 1. ERD 설계 - https://aquerytool.com/aquerymain/index/?rurl=f947f590-d659-4ca8-b075-c09e4db79a41 (비밀번호 : 7j1kcm) 
#### 2. EC2 & RDS 환경 구축 완료
#### 3. API 명세서 리스트업 완료 (약 40개 작성)
#### 4. dev/prod 서버 구축 (실직적으로 prod 서버 사용할 예정)
#### 5. SSL 구축
#### 6. API 역할 분담
#### 7. 더미데이터 (매우 기본적인 데이터만 추가)


## 2022.08.21
#### 1. 회원 API 개발 완료
#### 2. 좋아요 API 개발 완료
#### 3. 북마크 API 개발 90% 완료
#### 4. 더미데이터 - 데이터 조금 추가하여 테스트 진행
#### 5. 홈 화면 조회 API 개발 완료
#### 6. ERD 수정
#####   - Banner 테이블에 type 컬럼 추가
#####   - 각 종 테이블 default 값 추가
#####   - Company 테이블에 logoUrl 컬럼 추가
#### 7. 추가된 API 모두 명세서, 서버 반영 완료
#### 8. 깃헙 merge 이슈
#####    - BaseResponseStatus 에서 같은 에러 코드를 사용하여 발생
#####    - 각자 사용할 번호를 나누어 해결
#### 9. 서버 반영 시 빌드가 되지 않는 에러
#####  - build.gradle 수정으로 해결
#### 10. 클라이언트 개발자와 협의하여 API 우선순위 수정 -> 홈 화면 부터


## 2022.08.22
#### 1. 서브도메인 SSL 인증 문제 발생 -> nginx 설정에서 SSL 블록 추가해주어서 해결
#### 2. CORS 문제 발생 -> CORS 방침 설정해줌 (WebMvcConfig.java) 
#### 3. 홈 화면 조회 API에서 클라이언트 요청으로 배너 인덱스도 함께 보내도록 수정 완료
#### 4. 북마크, 채용 페이지 관련 조회에서 이미지가 여러개 출력되어버리는 에러 발생
#####     - 북마크 API 구조 변경 진행 중 - > 수정 완료.
#### 5. 채용 중인 포지션 조회하기 API 진행 중 
#### 6. 채용 중인 페이지 조회(회원,비회원) API 진행 중 (70%) -> 완료
#### 7. 더미데이터 대량 추가 (채용,회사,북마크,태그 등)
#### 8. 채용 중인 포지션 조회하기 클라이언트와 협의하여 구현 파트 협의
#### 9. 수정 완료한 API 명세서, 서버 반영 
#### 10. 채용중인 포지션 조회하기 (비회원용) API  
#### 11. 채용중인 포지션 조회하기 (회원용) API 완성
#### 12. 채용 정보 조회(상세페이지) API 완료.

## 1차 피드백
#### 1. 깃 헙 Branch 사용, 이슈 정리
##### 도메인 별로 각자 Branch 사용.  코드 리뷰 후 main과 merge
