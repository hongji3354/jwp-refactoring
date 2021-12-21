# 키친포스

## 요구 사항

### 상품 (product) - 메뉴를 관리하는 기준이 되는 데이터
- 상품을 등록할 수 있다.
  - 상품의 이름은 비어있을 수 없다.
  - 상품의 가격은 0원 이상이어야 한다.
- 상품 목록을 조회할 수 있다.

### 메뉴 그룹 (menu group) - 메뉴 묶음, 분류
- 메뉴 그룹을 등록할 수 있다.
  - 메뉴 그룹의 이름은 비어있을 수 없다.
- 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴 (menu) - 메뉴 그룹에 속하는 실제 주문 가능 단위
- 메뉴를 등록할 수 있다.
  - 메뉴의 이름은 비어있을 수 없다.
  - 메뉴의 가격은 0원 이상이어야 한다.
  - 메뉴 그룹이 존재해야 한다.
  - 메뉴에 등록하고자 하는 상품이 존재해야한다.
  - 메뉴의 금액은 상품의 총 금액(상품 금액*상품 개수)보다 작아야 한다.
- 메뉴 목록을 조회할 수 있다.

### 테이블 (table)
- 테이블을 생성할 수 있다.
  - 테이블 생성시 단체 미지정 상태로 변경한다.
- 테이블 목록을 조회할 수 있다.
- 테이블의 사용여부를 변경할 수 있다.
  - 사용여부를 변경하고자 하는 테이블이 존재해야 한다.
  - 테이블에 단체 지정이 되어 있다면 사용여부 변경이 불가능하다.
  - 주문상태가 조리 또는 식사면 사용여부 변경이 불가능하다.
- 방문한 손님 수를 변경할 수 있다.
  - 방문한 손님의 수는 0명 이상이여야 한다.
  - 방문한 손님의 수를 변경하고자 하는 테이블이 존재해야한다.
  - 테이블이 사용 가능한 상태여야 한다.

### 단체 지정 (table group)
- 단체 지정을 할 수 있다.
  - 단체 지정 하고자 하는 테이블의 개수가 2개 이상이여야 한다.
  - 단체 지정 하고자 하는 테이블이 모두 존재해야 한다.
  - 단체 지정 하고자 하는 테이블이 비어 있는 상태여야 한다.
  - 단체 지정 하고자 하는 테이블이 단체 지정 되어 있으면 안된다.
- 단체 지정을 해제를 할 수 있다.
  - 그룹에 속한 각 테이블의 주문 상태가 조리 또는 식사면 변경이 불가능 하다.

### 주문 (order)
- 주문을 할 수 있다.
    - 주문 항목이 존재해야 한다.
    - 주문 항목에 있는 메뉴가 실제 메뉴로 존재해야 한다.
    - 테이블이 존재해야한다.
    - 테이블이 사용 가능한 상태여야 한다.
    - 주문 테이블이 빈 테이블이면 안된다.
- 주문 목록을 조회할 수 있다.
- 주문 상태를 변경할 수 있다.
  - 주문이 존재해야 한다.
  - 주문의 상태가 완료이면 변경이 불가능 하다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
