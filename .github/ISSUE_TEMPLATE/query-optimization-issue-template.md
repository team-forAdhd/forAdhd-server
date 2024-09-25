---
name: Query Optimization Issue Template
about: 쿼리 최적화 이슈 템플릿
title: ''
labels: ''
assignees: ''

---

## ✔️ 최적화 대상 쿼리 

- **클래스명**: `UserRespository`
- **메소드 명**: `findByUserId`

## ✔️ 최적화 이유

ex. N + 1 문제 발생, timeout 발생 등 

## ✔️ 최적화 방안 

ex. repository 계층에서 관련 엔티티를 fetch join하여 조회
