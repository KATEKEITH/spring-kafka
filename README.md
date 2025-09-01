# ⚡ Kafka Consumer Tuning 실행 계획

## 1️⃣ 사전 준비
- [ ] Prometheus + Grafana로 CPU, Memory, Consumer Lag, 처리량(msg/sec) 모니터링 환경 구축
- [ ] 테스트용 트래픽 시뮬레이터 준비 (초당 20,000 메시지 기준)

---

## 2️⃣ 기본 세팅
- [ ] `max.poll.records=500` 적용 (CPU 안정화 우선)
- [ ] `fetch.max.bytes=16777216 (16MB)` 적용
- [ ] `max.partition.fetch.bytes=16777216 (16MB)` 적용
- [ ] Broker/Producer: `message.max.bytes=2MB`, `replica.fetch.max.bytes=2MB` 설정
- [ ] `compression.type=lz4` (또는 zstd) 적용 → 네트워크 트래픽 절감

---

## 3️⃣ 부하 테스트
- [ ] 초당 20,000 메시지 부하 테스트 실행
- [ ] CPU 사용률, Consumer Lag, 처리 속도 기록
- [ ] 80% 이상 CPU 점유 발생 시 Consumer scale-out 검토

---

## 4️⃣ 파라미터 튜닝
- [ ] `max.poll.records` 500 → 1000 범위 내 점진적 확대 (성능/CPU 비교)
- [ ] Consumer 인스턴스 수 조절 (파티션 수와 균형 유지)
- [ ] 처리량 / 안정성 균형점 찾기

---

## 5️⃣ 장애 대응 전략
- [ ] DLQ(Dead Letter Queue) 설정
- [ ] 재처리(Replay) 전략 수립 (재소비 시 idempotency 보장)

---

## 6️⃣ 운영 안정화
- [ ] 실시간 알람 (Lag, CPU, Memory, Throughput 기준)
- [ ] 주간 리포트: 평균 처리량, Lag, 장애 건수 기록
- [ ] 트래픽 패턴(피크 타임/비피크) 기반 Consumer 자동 확장 전략 수립
