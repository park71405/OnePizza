# OnePizza

<img src = "OnePizzaLogo.png" width="100%">

## 제작 컨셉
< 피자가게 자동화 가게 시스템 >
- java로만 이루어진 피자 주문 시스템입니다.
- 이 피자 가게는 로봇이 만들어주는 피자로 모든 제작과 재고 관리를 이 시스템을 통해 수행합니다.
<hr />
- 가게를 오픈할때 관리자는 관리자 메뉴로 로그인해 오픈을 눌러주어야 합니다.
- 마찬가지로 마감을 할 때에도 마감을 꼭 눌러주세요
<hr />
## 시나리오

### 회원
- 비회원은 회원가입을 할 수 있다.
- 회원 가입 형식은 이메일 아이디, 비밀번호, 휴대폰 번호, 주소로 구성되며 형식과 다르게 입력할 경우 오류 메시지가 출력된다.
- 로그인을 할 수 있다.
- 로그인 시 형식이 다를 경우 오류 메시지가 출력된다.
- 시스템을 통해 피자를 주문할 수 있다.
- 피자 종류(페퍼로니, 고구마, 쉬림프)를 선택해 장바구니에 넣을 수 있다.
- 피자 종류 선택 후 추가 메뉴(콜라, 피클, 핫소스)를 선택해 장바구니에 넣을 수 있다.
- 각각의 메뉴는 10개 이상 담을 수 없다.
- 요청사항을 남길 수 있다.
- 매장식사와 포장 중 선택할 수 있다.
- 장바구니를 확인하고 수량을 변경하거나 결제 혹은 장바구니 비우기를 선택할 수 있다.
- 선불로만 결제가 가능하며 카드 번호와 비밀번호를 입력할 수 있다.
- 정규표현식과 다를 경우 오류 메시지가 출력된다.
- 결제하지 않고 이전메뉴로 돌아갈 수 있다.
- 주문 내역 (메뉴, 가격, 주문 시각)을 확인할 수 있다.
- 각 피자의 대기수와 대기 시간을 확인 가능하다.

### 관리자
- 관리자용 계정은 Admin으로 등록되어 있다.
- 주문 내역을 조회할 수 있다.
- 하루, 한 달 매출을 확인 할 수 있다.
- 회원 리스트는 이메일 아이디와 비밀번호, 전화번호, 주소로 구성된다.
- 원하는 회원을 탈퇴시킬 수 있다.
- 매장을 오픈하고 마감할 수 있다.
- 매장 오픈, 마감 시간을 설정하거나 확인할 수 있다.
- 피자 재료의 재고를 확인할 수 있다.
- 피자 재료의 재고를 확인할 수 있다.

### 피자 기계
- 피자 재로의 재고를 기반으로 작동한다.
- 재고가 없을 경우 해당 재료가 들어가는 메뉴를 품절 상태로 변경할 수 있다.
- 주문 내역을 받아올 수 있다.
- 도우를 준비할 수 있다.
- 주문 내역에 맞는 토핑을 올릴 수 있다.
- 피자를 구울 수 있다.
- 피자를 포장할 수 있다.
- 피자를 내보낼 수 있다.

### 피자
- 모든 피자는 도우와 토마토 소스, 치즈를 가진다.
- 페퍼로니 피자는 추가로 페퍼로니와 파슬리를 가진다.
- 쉬림프 피자는 추가로 새우와 베이컨, 버섯, 마요네즈 소스, 파슬리를 가진다.
- 고구마 피자는 추가로 고구마와 양파, 머스타드 소스, 옥수수, 파슬리를 가진다.
