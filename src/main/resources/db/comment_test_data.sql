-- Comment 더미 데이터 (게시글별로 다양한 댓글수)
-- 1번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Spring Boot 정말 편하네요.', 1, 5, NULL),
('기본 설정이 명확합니다.', 1, 12, NULL),
('프로젝트 구조도 이해하기 쉬워요.', 1, 8, 1),
('다음 글도 기대합니다!', 1, 23, NULL),
('버전 업그레이드는 쉬운가요?', 1, 7, NULL),
('보안 설정은 어떻게 하나요?', 1, 15, NULL),
('마이크로서비스로 갈 수 있나요?', 1, 20, NULL),
('정말 도움 많이 됐습니다!', 1, 3, 3);

-- 2번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('영속성 컨텍스트 개념이 명확해졌어요.', 2, 7, NULL),
('1차 캐시 부분 설명이 좋네요.', 2, 15, NULL),
('더티 체킹은 어떻게 동작하나요?', 2, 19, NULL);

-- 3번 게시글 (12개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('인덱스 때문에 고민 중이었는데 해결됐어요.', 3, 4, NULL),
('복합 인덱스 설명이 명확합니다.', 3, 11, NULL),
('왼쪽 우선 규칙 처음 알았네요!', 3, 18, NULL),
('EXPLAIN으로 확인하는 방법도 궁금해요.', 3, 22, 12),
('실제 프로젝트에서 어떻게 적용하나요?', 3, 25, NULL),
('쿼리 튜닝 팁도 알려주세요.', 3, 28, NULL),
('인덱스 크기 최적화는?', 3, 2, NULL),
('통계 갱신 주기는?', 3, 6, NULL),
('파티셔닝과 함께 쓸 수 있나요?', 3, 9, NULL),
('성능 개선 효과가 크군요!', 3, 14, NULL),
('다른 DB도 비슷한가요?', 3, 17, NULL),
('정말 유용한 글입니다!', 3, 21, NULL);

-- 4번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('REST API 설계 시 항상 참고합니다.', 4, 6, NULL),
('URI 네이밍 규칙 부분이 유용했어요.', 4, 14, NULL);

-- 5번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Hook 사용하면서 헷갈렸던 부분이 정리됐어요.', 5, 9, NULL),
('useEffect 의존성 배열 설명 좋습니다!', 5, 16, NULL),
('커스텀 Hook 만드는 방법도 알려주세요.', 5, 21, NULL),
('useState는 어떻게 동작하나요?', 5, 3, NULL),
('렌더링 최적화 방법은?', 5, 10, NULL),
('Suspense와 함께 쓸 수 있나요?', 5, 13, NULL),
('useCallback vs useMemo는?', 5, 18, NULL),
('Context API는?', 5, 24, NULL),
('정말 도움 됐습니다!', 5, 27, NULL),
('더 많은 예제 기대합니다!', 5, 30, NULL);

-- 6번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Docker 입문자에게 최고의 글이네요.', 6, 10, NULL),
('컨테이너 개념이 이해됐습니다.', 6, 17, NULL),
('Dockerfile 작성법도 다뤄주세요!', 6, 24, NULL),
('좋은 제안 감사합니다.', 6, 3, NULL),
('이미지 최적화는?', 6, 11, NULL);

-- 7번 게시글 (15개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Git Flow 복잡하다고 느꼈는데 이해했어요.', 7, 2, NULL),
('우리 팀은 Github Flow 쓰는데 참고됩니다.', 7, 13, NULL),
('브랜치 전략 선택 기준이 명확하네요.', 7, 19, NULL),
('릴리즈 브랜치는 어떻게 관리하나요?', 7, 25, NULL),
('핫픽스 브랜치도 있나요?', 7, 5, NULL),
('롤백은 어떻게 하나요?', 7, 8, NULL),
('태그 관리는 어떻게?', 7, 12, NULL),
('머지 충돌은 어떻게 해결?', 7, 15, NULL),
('리베이스와 머지 차이는?', 7, 18, NULL),
('스쿼시 머지는?', 7, 22, NULL),
('깃 훅은 어떻게?', 7, 26, NULL),
('CI/CD 연동은?', 7, 29, NULL),
('깃 운영 정말 중요하네요!', 7, 3, NULL),
('더 많은 팁 부탁합니다!', 7, 7, NULL),
('감사합니다!', 7, 9, NULL);

-- 8번 게시글 (1개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('EC2 배포 성공했습니다! 감사합니다.', 8, 5, NULL);

-- 9번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('JWT 구현하면서 막혔던 부분이 해결됐어요.', 9, 1, NULL),
('RefreshToken 관리 방법이 명확하네요.', 9, 15, NULL),
('토큰 만료 시간은 보통 얼마로 설정하나요?', 9, 20, NULL),
('AccessToken 1시간, RefreshToken 7일 정도 권장합니다.', 9, 5, 39),
('토큰 탈취는 어떻게 방지?', 9, 10, NULL),
('HTTPS는 필수인가요?', 9, 16, NULL),
('정말 유용합니다!', 9, 21, NULL);

-- 10번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('테스트 코드의 중요성을 다시 느낍니다.', 10, 3, NULL),
('Mockito 사용법이 잘 설명되어 있어요.', 10, 14, NULL),
('@Mock과 @InjectMocks 차이가 궁금해요.', 10, 22, NULL),
('@Mock은 모의 객체, @InjectMocks는 주입 대상입니다.', 10, 5, 43),
('이해됐습니다. 감사합니다!', 10, 22, 44),
('통합 테스트는?', 10, 8, NULL),
('E2E 테스트는?', 10, 12, NULL),
('테스트 커버리지는?', 10, 18, NULL),
('CI에서 자동 테스트는?', 10, 24, NULL),
('정말 필수 지식이네요!', 10, 27, NULL),
('감사합니다!', 10, 30, NULL);

-- 11번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('논리적 삭제가 정말 유용하네요.', 11, 8, NULL),
('데이터 복구도 쉽고 좋습니다.', 11, 16, NULL),
('성능 영향은 없나요?', 11, 24, NULL),
('인덱스를 잘 설계하면 괜찮습니다.', 11, 5, 48);

-- 12번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CORS 에러로 몇 시간을 날렸는데 해결됐어요.', 12, 7, NULL),
('Spring에서 설정하는 방법이 명확합니다.', 12, 13, NULL),
('프리플라이트 요청도 잘 설명되어 있네요.', 12, 19, NULL),
('질문이 있는데 답변 가능할까요?', 12, 6, 52),
('네, 댓글로 남겨주세요!', 12, 5, 53),
('OPTIONS 메서드는?', 12, 11, NULL),
('Credentials는?', 12, 15, NULL),
('커스텀 헤더는?', 12, 20, NULL),
('정말 도움됩니다!', 12, 25, NULL);

-- 13번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Redis 캐싱으로 성능이 확 올라갔어요.', 13, 2, NULL),
('캐시 무효화 전략이 유용합니다.', 13, 17, NULL),
('TTL 설정 기준도 알려주세요.', 13, 21, NULL),
('데이터 특성에 맞게 설정하면 됩니다.', 13, 5, 57),
('메모리 관리는?', 13, 10, NULL),
('감사합니다!', 13, 14, NULL);

-- 14번 게시글 (13개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SQL과 NoSQL 차이 정확히 이해했어요.', 14, 4, NULL),
('각각의 장단점이 명확하네요.', 14, 10, NULL),
('어떤 상황에 어떤 것을 써야 하나요?', 14, 18, NULL),
('프로젝트 요구사항에 따라 결정하면 됩니다.', 14, 5, NULL),
('좋은 가이드 감사합니다!', 14, 18, 62),
('확장성은?', 14, 7, NULL),
('비용은?', 14, 12, NULL),
('운영 난이도는?', 14, 17, NULL),
('스키마는?', 14, 21, NULL),
('쿼리 성능은?', 14, 23, NULL),
('라이센스는?', 14, 26, NULL),
('커뮤니티는?', 14, 28, NULL),
('정말 좋은 비교입니다!', 14, 31, NULL);

-- 15번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Github Actions로 CI/CD 구축했어요.', 15, 6, NULL),
('자동화 배포가 정말 편하네요.', 15, 12, NULL),
('테스트 자동화도 가능한가요?', 15, 23, NULL);

-- 16번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Nginx 로드밸런싱 설정이 명확합니다.', 16, 9, NULL),
('리버스 프록시 개념이 이해됐어요.', 16, 14, NULL),
('무중단 배포도 가능한가요?', 16, 20, NULL),
('헬스 체크와 함께 사용하면 가능합니다.', 16, 5, NULL),
('정말 유용한 팁이 많네요!', 16, 20, 71),
('스트림 캐싱은?', 16, 8, NULL),
('gzip 압축은?', 16, 13, NULL),
('감사합니다!', 16, 18, NULL);

-- 17번 게시글 (14개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('TypeScript로 마이그레이션하는 중입니다.', 17, 1, NULL),
('타입 안전성이 정말 좋네요.', 17, 11, NULL),
('초보자도 배우기 쉬운가요?', 17, 19, NULL),
('JavaScript 경험이 있으면 금방 배웁니다!', 17, 5, 75),
('Generics는?', 17, 7, NULL),
('Interfaces는?', 17, 9, NULL),
('Decorators는?', 17, 12, NULL),
('Type Guards는?', 17, 15, NULL),
('Union Types는?', 17, 17, NULL),
('Intersection Types는?', 17, 20, NULL),
('Enums는?', 17, 22, NULL),
('Modules는?', 17, 24, NULL),
('정말 강력한 언어네요!', 17, 26, NULL),
('감사합니다!', 17, 28, NULL);

-- 18번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SSR의 이점이 명확하네요.', 18, 5, NULL),
('SEO 최적화도 가능하군요.', 18, 15, NULL);

-- 19번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('BeautifulSoup으로 크롤링 시작했어요.', 19, 3, NULL),
('웹 데이터 수집이 이렇게 쉽다니!', 19, 16, NULL),
('법적 문제는 없나요?', 19, 21, NULL),
('robots.txt와 이용약관을 반드시 확인하세요.', 19, 5, 84),
('Selenium은?', 19, 8, NULL),
('Puppeteer는?', 19, 11, NULL),
('데이터 파싱은?', 19, 14, NULL),
('에러 처리는?', 19, 17, NULL),
('속도 최적화는?', 19, 20, NULL),
('감사합니다!', 19, 23, NULL);

-- 20번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Pandas로 데이터 전처리하는 중입니다.', 20, 7, NULL),
('DataFrame 조작이 정말 직관적이네요.', 20, 13, NULL),
('대용량 데이터 처리는 어떻게 하나요?', 20, 25, NULL),
('Dask나 Spark을 고려해보세요.', 20, 5, NULL),
('유용한 팁 감사합니다!', 20, 25, 89),
('GroupBy는?', 20, 9, NULL),
('Merge는?', 20, 12, NULL);

-- 21번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('NoSQL 스키마 설계가 명확해졌어요.', 21, 2, NULL),
('임베딩 vs 참조 개념이 좋습니다.', 21, 17, NULL),
('정규화가 항상 답은 아니네요.', 21, 23, NULL),
('맞습니다! 유연한 설계가 중요합니다.', 21, 5, 93),
('인덱싱은?', 21, 6, NULL),
('샤딩은?', 21, 10, NULL),
('복제는?', 21, 14, NULL),
('트랜잭션은?', 21, 19, NULL),
('일관성은?', 21, 22, NULL),
('성능은?', 21, 26, NULL),
('감사합니다!', 21, 29, NULL);

-- 22번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('GraphQL 장점이 정말 많네요.', 22, 4, NULL),
('쿼리 최적화가 쉬워졌어요.', 22, 12, NULL),
('REST API에서 마이그레이션 어렵지 않나요?', 22, 18, NULL),
('단계적으로 도입할 수 있습니다.', 22, 5, NULL);

-- 23번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Kotlin 코루틴 정말 멋있네요.', 23, 8, NULL),
('async/await 문법이 직관적입니다.', 23, 14, NULL),
('Java와의 상호운용성도 좋고요.', 23, 19, NULL),
('Kotlin은 정말 좋은 언어입니다!', 23, 5, 102),
('Extension Functions는?', 23, 7, NULL),
('Data Classes는?', 23, 11, NULL),
('Sealed Classes는?', 23, 15, NULL),
('Delegation은?', 23, 18, NULL),
('감사합니다!', 23, 21, NULL);

-- 24번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Jetpack Compose 배우는 중입니다.', 24, 6, NULL),
('선언형 UI가 훨씬 간단하네요.', 24, 11, NULL),
('Material Design3 지원도 좋아요.', 24, 20, NULL),
('상태 관리가 직관적입니다!', 24, 5, NULL),
('레이아웃 코드가 너무 깔끔해졌어요.', 24, 20, 107);

-- 25번 게시글 (12개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Vue.js 정말 배우기 좋네요.', 25, 9, NULL),
('문법이 간단하고 우아합니다.', 25, 19, NULL),
('React보다 낮은 진입장벽이 좋아요.', 25, 25, NULL),
('Vue는 처음 배우기 정말 좋습니다!', 25, 5, 111),
('v-model은?', 25, 7, NULL),
('computed는?', 25, 10, NULL),
('watch는?', 25, 13, NULL),
('lifecycle hooks는?', 25, 16, NULL),
('component는?', 25, 18, NULL),
('props는?', 25, 21, NULL),
('emit은?', 25, 23, NULL),
('감사합니다!', 25, 26, NULL);

-- 26번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Vuex로 상태 관리 정말 편하네요.', 26, 10, NULL),
('State, Mutation, Action 구분이 명확합니다.', 26, 20, NULL),
('Pinia는 어떤가요?', 26, 26, NULL),
('Pinia도 좋지만 Vuex도 충분합니다.', 26, 5, 115),
('모두 시도해봐야겠어요!', 26, 26, 116),
('감사합니다!', 26, 28, NULL);

-- 27번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Flutter 위젯 종류가 이렇게 많네요.', 27, 11, NULL),
('StatefulWidget과 StatelessWidget 차이 이해했어요.', 27, 21, NULL),
('기본 위젯들이 잘 정리되어 있습니다.', 27, 1, NULL),
('Flutter 개발할 때 참고하겠습니다!', 27, 5, 120),
('Material Design은?', 27, 8, NULL),
('Cupertino는?', 27, 12, NULL),
('Custom Widgets는?', 27, 15, NULL),
('감사합니다!', 27, 18, NULL);

-- 28번 게시글 (15개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Dart 언어가 생각보다 강력하네요.', 28, 12, NULL),
('null safety 기능이 정말 좋습니다.', 28, 22, NULL),
('타입 추론도 잘 동작해요.', 28, 2, NULL),
('비동기 처리도 간단하고요.', 28, 5, NULL),
('Dart로 Flutter 개발하기 정말 좋아요!', 28, 22, 125),
('Variables는?', 28, 7, NULL),
('Functions는?', 28, 10, NULL),
('Classes는?', 28, 13, NULL),
('Mixins는?', 28, 16, NULL),
('Extensions는?', 28, 19, NULL),
('Generics는?', 28, 21, NULL),
('Type inference는?', 28, 24, NULL),
('Pattern matching은?', 28, 26, NULL),
('Records는?', 28, 28, NULL),
('정말 좋은 언어입니다!', 28, 31, NULL);

-- 29번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Elasticsearch 검색 속도가 놀라워요.', 29, 13, NULL),
('인덱싱 방식이 효율적입니다.', 29, 23, NULL),
('전문 검색 기능이 정말 강력합니다.', 29, 3, NULL);

-- 30번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Kafka 처리량이 정말 크네요.', 30, 14, NULL),
('파티션 구조가 확장성 좋습니다.', 30, 24, NULL),
('컨슈머 그룹 관리도 유연하고요.', 30, 4, NULL),
('분산 시스템에 최적화되어 있어요.', 30, 5, NULL),
('고가용성 시스템 구축할 때 추천합니다!', 30, 24, 134),
('Offset은?', 30, 8, NULL),
('Replication은?', 30, 11, NULL),
('Retention은?', 30, 15, NULL),
('Throughput은?', 30, 18, NULL),
('감사합니다!', 30, 21, NULL);

-- 31번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Linux 명령어 모음이 정말 유용해요.', 31, 1, NULL),
('모든 필수 명령어가 다 있네요.', 31, 11, NULL),
('예제도 실용적이고 좋습니다.', 31, 21, NULL),
('리눅스 초보자 강추합니다!', 31, 5, 138),
('권한 관리는?', 31, 7, NULL),
('파일 시스템은?', 31, 10, NULL),
('감사합니다!', 31, 14, NULL);

-- 32번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Shell Script로 자동화하니 정말 편하네요.', 32, 2, NULL),
('배치 작업 작성이 간단해졌어요.', 32, 12, NULL),
('스크립트 최적화 팁도 좋아요.', 32, 22, NULL),
('크론 작업과 함께 사용하면 최고예요.', 32, 5, NULL),
('시스템 관리자 필독서네요!', 32, 22, 143),
('Variables는?', 32, 6, NULL),
('Functions는?', 32, 9, NULL),
('Loops는?', 32, 13, NULL),
('Conditionals는?', 32, 16, NULL),
('Error Handling은?', 32, 19, NULL),
('감사합니다!', 32, 23, NULL);

-- 33번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('PostgreSQL이 더 강력하네요.', 33, 3, NULL),
('JSON 지원이 정말 유용합니다.', 33, 13, NULL),
('Advanced 기능들이 많고요.', 33, 23, NULL),
('엔터프라이즈 프로젝트에 좋아요!', 33, 5, 147);

-- 34번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('인덱스 설계가 성능에 큰 영향이네요.', 34, 4, NULL),
('B-tree 원리를 이해하니 설계가 쉬워요.', 34, 14, NULL),
('복합 인덱스 활용법이 실무적이네요.', 34, 24, NULL),
('EXPLAIN으로 쿼리 분석이 가능하군요.', 34, 9, NULL),
('DB 성능 최적화 필수 지식이에요!', 34, 5, 152),
('Hash Index는?', 34, 7, NULL),
('Full Text Index는?', 34, 11, NULL),
('Partial Index는?', 34, 16, NULL),
('감사합니다!', 34, 20, NULL);

-- 35번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('gRPC 성능이 정말 빠르네요.', 35, 5, NULL),
('Protocol Buffer가 효율적입니다.', 35, 15, NULL),
('REST보다 대역폭 사용이 적어요.', 35, 25, NULL),
('마이크로서비스 아키텍처 필수군요!', 35, 5, 156),
('감사합니다!', 35, 28, NULL);

-- 36번 게시글 (13개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('WebSocket으로 실시간 통신이 정말 좋아요.', 36, 6, NULL),
('채팅 기능 구현이 간단하네요.', 36, 16, NULL),
('Heartbeat 설정도 중요하군요.', 36, 26, NULL),
('재연결 로직은 어떻게 구현하나요?', 36, 2, NULL),
('클라이언트 측에서 처리하면 됩니다!', 36, 5, 161),
('메시지 포맷은?', 36, 8, NULL),
('보안은?', 36, 11, NULL),
('성능은?', 36, 14, NULL),
('확장성은?', 36, 17, NULL),
('모니터링은?', 36, 20, NULL),
('에러 처리는?', 36, 23, NULL),
('테스트는?', 36, 25, NULL),
('감사합니다!', 36, 28, NULL);

-- 37번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('RabbitMQ 신뢰성이 정말 높네요.', 37, 7, NULL),
('메시지 라우팅이 유연합니다.', 37, 17, NULL);

-- 38번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('MSA 설계 방법이 명확하네요.', 38, 8, NULL),
('서비스 분할 기준이 좋습니다.', 38, 18, NULL),
('API 게이트웨이의 역할이 중요하군요.', 38, 28, NULL),
('서비스 간 통신 복잡도는 어떻게 처리해요?', 38, 3, NULL),
('메시지 큐와 이벤트 기반 아키텍처를 추천합니다!', 38, 5, 170),
('Saga 패턴은?', 38, 10, NULL),
('서킷 브레이커는?', 38, 15, NULL),
('감사합니다!', 38, 22, NULL);

-- 39번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('OAuth 2.0 플로우 정말 명확해요.', 39, 9, NULL),
('소셜 로그인 구현이 간단합니다.', 39, 19, NULL),
('보안이 잘 고려되어 있네요.', 39, 29, NULL),
('현대 웹 애플리케이션 필수예요!', 39, 5, 174),
('Authorization Code는?', 39, 6, NULL),
('Implicit Flow는?', 39, 12, NULL),
('Client Credentials는?', 39, 16, NULL),
('Refresh Token은?', 39, 21, NULL),
('PKCE는?', 39, 25, NULL),
('감사합니다!', 39, 27, NULL);

-- 40번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Spring Security 설정이 강력하네요.', 40, 10, NULL),
('CSRF, CORS 보호가 자동으로 되고요.', 40, 20, NULL),
('커스텀 인증도 쉽게 구현할 수 있어요.', 40, 30, NULL),
('JWT와 조합하면 정말 좋습니다.', 40, 5, NULL),
('보안 강화 필수 프레임워크예요!', 40, 20, 179),
('감사합니다!', 40, 35, NULL);

-- 41번 게시글 (14개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('DDD에서 Aggregate Root 설계가 가장 중요하다고 느꼈어요.', 41, 3, NULL),
('Bounded Context 분리 기준이 명확해졌습니다.', 41, 12, NULL),
('Value Object와 Entity 구분이 항상 어렵네요.', 41, 18, NULL),
('불변성 여부로 판단하면 쉽습니다.', 41, 7, NULL),
('도메인 이벤트 발행 패턴도 설명해주세요.', 41, 25, NULL),
('Repository 패턴은 DDD에서 핵심이죠.', 41, 30, NULL),
('전략적 설계와 전술적 설계 차이가 잘 정리되어 있어요.', 41, 14, NULL),
('Ubiquitous Language 정의가 팀 소통에 큰 도움이 됩니다.', 41, 42, NULL),
('Anti-Corruption Layer는 언제 사용하나요?', 41, 8, NULL),
('레거시 시스템 연동 시 필수적입니다.', 41, 5, NULL),
('Factory 패턴으로 복잡한 Aggregate 생성을 단순화할 수 있어요.', 41, 33, NULL),
('Domain Service는 어떤 경우에 쓰나요?', 41, 19, NULL),
('여러 Aggregate에 걸친 비즈니스 로직 처리 시 사용합니다.', 41, 11, NULL),
('정말 실무에 도움되는 글이네요!', 41, 45, NULL);

-- 42번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CQRS로 읽기와 쓰기 모델을 분리하니 성능이 좋아졌어요.', 42, 5, NULL),
('Command와 Query 분리 개념이 깔끔하게 정리되어 있네요.', 42, 22, NULL),
('이벤트 소싱과 함께 적용하면 더 효과적이겠네요.', 42, 37, NULL);

-- 43번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Event Sourcing으로 모든 상태 변경을 추적할 수 있어서 좋아요.', 43, 6, NULL),
('이벤트 스토어 설계 방법이 궁금합니다.', 43, 15, NULL),
('스냅샷을 활용하면 이벤트 리플레이 성능 문제를 해결할 수 있어요.', 43, 22, NULL),
('CQRS와 함께 적용하는 사례가 많더라고요.', 43, 33, NULL),
('이벤트 버전 관리는 어떻게 하나요?', 43, 9, NULL),
('Upcasting 기법을 사용하면 됩니다.', 43, 40, NULL),
('실제 프로덕션 적용 시 주의사항이 잘 정리되어 있어요.', 43, 28, NULL);

-- 44번 게시글 (12개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('헥사고날 아키텍처로 외부 의존성을 완전히 분리할 수 있네요.', 44, 2, NULL),
('Port와 Adapter 개념이 직관적으로 이해됐어요.', 44, 10, NULL),
('인바운드와 아웃바운드 어댑터 구분이 명확합니다.', 44, 17, NULL),
('테스트하기 정말 편해졌어요.', 44, 24, NULL),
('기존 레이어드 아키텍처와 비교 설명이 좋았습니다.', 44, 5, NULL),
('도메인 로직이 순수해져서 유지보수가 쉬워졌어요.', 44, 31, NULL),
('Spring에서 적용하는 구체적인 예제가 도움됐어요.', 44, 13, NULL),
('Use Case 인터페이스 설계 방법이 깔끔하네요.', 44, 38, NULL),
('DIP 원칙이 자연스럽게 적용되는 구조군요.', 44, 8, NULL),
('인프라 계층 교체가 자유로워서 좋습니다.', 44, 21, NULL),
('실제 프로젝트에 바로 적용해봐야겠어요.', 44, 44, NULL),
('아키텍처 관련 최고의 글이에요!', 44, 16, NULL);

-- 45번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('TDD 사이클인 Red-Green-Refactor가 명확해졌어요.', 45, 9, NULL),
('테스트를 먼저 작성하니 설계가 자연스럽게 좋아지네요.', 45, 16, NULL),
('실무에서 TDD 적용이 처음엔 느리지만 결국 빨라져요.', 45, 21, NULL),
('Given-When-Then 패턴 예제가 이해하기 쉬웠어요.', 45, 35, NULL),
('테스트 커버리지 목표를 어느 정도로 잡으면 될까요?', 45, 42, NULL);

-- 46번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('의미 있는 변수명의 중요성을 다시 느꼈어요.', 46, 4, NULL),
('함수는 하나의 일만 해야 한다는 원칙이 핵심이네요.', 46, 11, NULL),
('주석보다 코드 자체가 문서가 되어야 한다는 말에 공감합니다.', 46, 18, NULL),
('SRP 원칙을 코드 레벨에서 적용하는 방법이 좋아요.', 46, 25, NULL),
('보이스카우트 규칙 실천하고 있어요.', 46, 32, NULL),
('중복 코드 제거 기법이 실용적이에요.', 46, 7, NULL),
('에러 처리 패턴 정리가 깔끔합니다.', 46, 14, NULL),
('경계 조건 처리 방법이 도움됐어요.', 46, 39, NULL),
('클린 코드 리뷰 체크리스트가 유용하네요.', 46, 46, NULL),
('팀 전체에 공유하겠습니다.', 46, 23, NULL),
('이 글을 읽고 코드 품질이 확 올라갔어요!', 46, 50, NULL);

-- 47번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Extract Method 기법을 자주 사용하게 되네요.', 47, 6, NULL),
('Rename Variable이 가장 기본이면서 효과적인 리팩토링이에요.', 47, 19, NULL),
('레거시 코드에 테스트를 먼저 작성하고 리팩토링하는 순서가 중요하군요.', 47, 28, NULL),
('IDE 리팩토링 단축키 모음도 유용합니다.', 47, 41, NULL);

-- 48번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Strategy 패턴으로 알고리즘 교체가 유연해졌어요.', 48, 3, NULL),
('Observer 패턴이 이벤트 시스템에 딱이네요.', 48, 13, NULL),
('Factory Method와 Abstract Factory 차이가 명확해졌습니다.', 48, 20, NULL),
('Singleton은 주의해서 사용해야 한다는 점 동의합니다.', 48, 27, NULL),
('Decorator 패턴으로 기능 확장이 깔끔해지네요.', 48, 8, NULL),
('Template Method 패턴이 프레임워크 설계에 많이 쓰이는군요.', 48, 34, NULL),
('Builder 패턴으로 복잡한 객체 생성이 간단해져요.', 48, 15, NULL),
('Proxy 패턴과 Decorator 패턴 차이를 이제 이해했어요.', 48, 43, NULL),
('패턴별 실제 코드 예제가 정말 도움됩니다!', 48, 29, NULL);

-- 49번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('스프린트 플래닝에서 스토리 포인트 산정이 항상 어렵네요.', 49, 7, NULL),
('데일리 스크럼을 15분 안에 끝내는 팁이 좋아요.', 49, 17, NULL),
('회고를 제대로 하면 팀이 정말 성장합니다.', 49, 24, NULL),
('칸반 보드 활용법이 실용적이에요.', 49, 36, NULL),
('번다운 차트로 진행 상황을 추적하니 좋아요.', 49, 10, NULL),
('애자일은 문화가 먼저라는 말에 공감합니다.', 49, 48, NULL);

-- 50번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('코드 리뷰 체크리스트가 실용적이에요.', 50, 5, NULL),
('리뷰 시 감정이 아닌 코드에 집중하는 것이 중요하죠.', 50, 15, NULL),
('PR 크기를 작게 유지하는 것이 핵심이네요.', 50, 22, NULL),
('자동화 도구로 스타일 체크를 먼저 하면 효율적이에요.', 50, 31, NULL),
('페어 프로그래밍과 코드 리뷰의 차이점이 흥미롭네요.', 50, 9, NULL),
('건설적인 피드백 예시가 도움됐습니다.', 50, 38, NULL),
('리뷰 문화 정착에 시간이 걸리지만 효과는 확실해요.', 50, 44, NULL),
('우리 팀에도 적용해보겠습니다!', 50, 19, NULL);

-- 51번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('JMeter로 부하 테스트하니 병목 지점을 바로 찾았어요.', 51, 8, NULL),
('Gatling은 스크립트 기반이라 CI/CD 연동이 편하네요.', 51, 21, NULL),
('nGrinder도 국내에서 많이 사용하더라고요.', 51, 34, NULL),
('성능 테스트 결과 리포트 분석 방법이 유용합니다.', 51, 47, NULL);

-- 52번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Prometheus와 Grafana 조합이 최강이에요.', 52, 3, NULL),
('메트릭 수집 전략이 잘 정리되어 있네요.', 52, 16, NULL),
('알림 규칙 설정이 운영에서 정말 중요합니다.', 52, 29, NULL),
('대시보드 템플릿 공유 부탁드려요.', 52, 42, NULL),
('APM 도구와의 연동 방법도 궁금합니다.', 52, 11, NULL);

-- 53번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('구조화된 로깅이 검색과 분석에 훨씬 유리하네요.', 53, 7, NULL),
('로그 레벨 기준이 명확해졌어요.', 53, 20, NULL),
('ELK 스택 구축 가이드가 실용적입니다.', 53, 33, NULL),
('민감 정보 마스킹 처리 방법이 중요하군요.', 53, 46, NULL);

-- 54번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Sentry로 에러 추적하니 대응 속도가 빨라졌어요.', 54, 5, NULL),
('에러 그룹핑과 우선순위 지정이 편하네요.', 54, 18, NULL),
('슬랙 연동으로 실시간 알림 받고 있어요.', 54, 31, NULL),
('소스맵 업로드로 정확한 에러 위치를 파악할 수 있어요.', 54, 44, NULL),
('릴리즈별 에러 트래킹이 유용합니다.', 54, 12, NULL);

-- 55번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('K6로 부하 테스트 시나리오 작성이 간편해요.', 55, 9, NULL),
('Ramp-up 패턴 설정 방법이 실무적이네요.', 55, 22, NULL),
('임계치 설정과 SLA 기준 정의가 중요하군요.', 55, 35, NULL),
('CI/CD 파이프라인에 부하 테스트를 포함시키는 방법이 좋아요.', 55, 48, NULL);

-- 56번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('HikariCP 설정 최적화로 응답 시간이 단축됐어요.', 56, 2, NULL),
('커넥션 풀 사이즈 산정 공식이 유용합니다.', 56, 15, NULL),
('커넥션 누수 감지 설정이 중요하네요.', 56, 28, NULL),
('타임아웃 설정 기준이 명확해졌어요.', 56, 41, NULL),
('모니터링 메트릭 연동 방법도 감사합니다.', 56, 8, NULL);

-- 57번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Cache-Aside 패턴이 가장 범용적이네요.', 57, 6, NULL),
('Write-Through와 Write-Behind 차이가 명확해졌어요.', 57, 19, NULL),
('TTL 기반 만료 전략과 이벤트 기반 무효화 비교가 좋습니다.', 57, 32, NULL),
('캐시 스탬피드 방지 기법이 실무에서 중요하군요.', 57, 45, NULL);

-- 58번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('2PC와 Saga 패턴 비교가 잘 되어 있네요.', 58, 4, NULL),
('보상 트랜잭션 설계가 핵심이라는 점 동의합니다.', 58, 17, NULL),
('Choreography vs Orchestration 방식 설명이 좋아요.', 58, 30, NULL),
('Outbox 패턴으로 이벤트 발행의 신뢰성을 보장할 수 있군요.', 58, 43, NULL),
('실무에서 가장 어려운 부분인데 잘 정리해주셨어요.', 58, 10, NULL);

-- 59번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Spring Cloud Gateway로 라우팅 설정이 간편해요.', 59, 8, NULL),
('Rate Limiting 필터 적용 방법이 실용적이네요.', 59, 21, NULL),
('인증 토큰 검증을 게이트웨이에서 처리하니 효율적이에요.', 59, 34, NULL),
('로드밸런싱과 서킷 브레이커 통합 설정이 유용합니다.', 59, 47, NULL);

-- 60번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Istio로 서비스 간 통신 관리가 한결 편해졌어요.', 60, 3, NULL),
('사이드카 프록시 개념이 명확하게 이해됐습니다.', 60, 16, NULL),
('트래픽 관리와 카나리 배포가 쉬워지네요.', 60, 29, NULL),
('mTLS로 서비스 간 보안이 강화됐어요.', 60, 42, NULL),
('Observability 기능이 기본 제공되는 게 좋습니다.', 60, 11, NULL);

-- 61번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Kubernetes 클러스터 운영 노하우가 잘 정리되어 있어요.', 61, 7, NULL),
('Pod 스케줄링 전략 설명이 실무적이네요.', 61, 20, NULL),
('HPA로 오토스케일링 설정하니 트래픽 대응이 자동화됐어요.', 61, 33, NULL),
('리소스 요청과 제한 설정 기준이 유용합니다.', 61, 46, NULL);

-- 62번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('로컬 개발 환경을 Docker Compose로 통일하니 온보딩이 쉬워졌어요.', 62, 5, NULL),
('멀티 컨테이너 구성 예제가 실용적이네요.', 62, 18, NULL),
('볼륨 마운트와 네트워크 설정 방법이 명확합니다.', 62, 31, NULL),
('환경별 override 파일 활용법이 좋아요.', 62, 44, NULL),
('docker-compose.yml 작성 베스트 프랙티스가 도움됐어요.', 62, 12, NULL);

-- 63번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Helm 템플릿 문법이 처음엔 어렵지만 익숙해지면 강력하네요.', 63, 9, NULL),
('values.yaml로 환경별 설정 관리가 깔끔해졌어요.', 63, 22, NULL),
('Chart 의존성 관리 방법이 유용합니다.', 63, 35, NULL),
('Helm Hook으로 배포 라이프사이클 제어가 가능하군요.', 63, 48, NULL);

-- 64번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('ArgoCD로 GitOps 워크플로우를 구축하니 배포가 투명해졌어요.', 64, 2, NULL),
('Git 리포지토리가 단일 진실 공급원이 되는 개념이 좋습니다.', 64, 15, NULL),
('Sync 정책과 자동 복구 기능이 안정적이에요.', 64, 28, NULL),
('멀티 클러스터 관리도 ArgoCD로 할 수 있군요.', 64, 41, NULL),
('Application Set으로 대규모 배포 관리가 편해졌어요.', 64, 8, NULL);

-- 65번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Terraform으로 인프라를 코드로 관리하니 재현성이 보장돼요.', 65, 6, NULL),
('State 파일 관리가 핵심이라는 점 동의합니다.', 65, 19, NULL),
('모듈화로 재사용 가능한 인프라 코드를 만들 수 있어요.', 65, 32, NULL),
('Plan과 Apply 단계 분리가 안전한 변경을 가능하게 하네요.', 65, 45, NULL);

-- 66번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Ansible Playbook으로 서버 설정 자동화가 정말 편해요.', 66, 4, NULL),
('Idempotent한 실행이 보장되는 점이 좋습니다.', 66, 17, NULL),
('Role 기반 구조화로 재사용성이 높아졌어요.', 66, 30, NULL),
('인벤토리 관리 방법이 실용적이네요.', 66, 43, NULL),
('Vault와 연동한 시크릿 관리도 유용합니다.', 66, 10, NULL);

-- 67번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Lambda로 서버 관리 없이 코드만 배포하니 편리해요.', 67, 8, NULL),
('콜드 스타트 최적화 방법이 실무에서 중요하네요.', 67, 21, NULL),
('Lambda Layer로 공통 라이브러리를 관리할 수 있군요.', 67, 34, NULL),
('비용 최적화 팁이 유용합니다.', 67, 47, NULL);

-- 68번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('S3 버킷 정책과 ACL 설정 방법이 명확해요.', 68, 3, NULL),
('수명 주기 정책으로 스토리지 비용을 절감할 수 있네요.', 68, 16, NULL),
('멀티파트 업로드로 대용량 파일 처리가 효율적이에요.', 68, 29, NULL),
('Pre-signed URL로 안전한 파일 공유가 가능하군요.', 68, 42, NULL),
('크로스 리전 복제 설정이 재해 복구에 유용합니다.', 68, 11, NULL);

-- 69번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CloudFront로 정적 콘텐츠 전송 속도가 확 빨라졌어요.', 69, 7, NULL),
('캐시 무효화 전략과 TTL 설정이 중요하네요.', 69, 20, NULL),
('Origin Access Identity로 S3 직접 접근을 차단할 수 있군요.', 69, 33, NULL),
('Lambda@Edge로 엣지에서 커스텀 로직 실행이 가능하네요.', 69, 46, NULL);

-- 70번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Route 53의 다양한 라우팅 정책이 잘 정리되어 있어요.', 70, 5, NULL),
('가중치 기반 라우팅으로 카나리 배포가 가능하군요.', 70, 18, NULL),
('헬스 체크와 연동한 장애 조치 설정이 중요하네요.', 70, 31, NULL),
('도메인 등록부터 DNS 설정까지 한 곳에서 관리할 수 있어요.', 70, 44, NULL),
('지연 시간 기반 라우팅으로 글로벌 서비스에 유용합니다.', 70, 12, NULL);

-- 71번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('RDS Multi-AZ 구성으로 고가용성을 확보할 수 있네요.', 71, 9, NULL),
('Read Replica로 읽기 성능을 향상시키는 방법이 유용해요.', 71, 22, NULL),
('Parameter Group 튜닝 포인트가 잘 정리되어 있습니다.', 71, 35, NULL),
('자동 백업과 스냅샷 관리 전략이 중요하군요.', 71, 48, NULL);

-- 72번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('DynamoDB의 파티션 키 설계가 성능의 핵심이네요.', 72, 2, NULL),
('On-demand 모드와 Provisioned 모드 비교가 좋습니다.', 72, 15, NULL),
('GSI와 LSI 활용 전략이 실무에서 중요해요.', 72, 28, NULL),
('TTL 기능으로 데이터 자동 정리가 편하네요.', 72, 41, NULL),
('DynamoDB Streams로 이벤트 기반 처리가 가능하군요.', 72, 8, NULL);

-- 73번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('ElastiCache Redis 클러스터 모드로 확장성이 좋아졌어요.', 73, 6, NULL),
('세션 스토어로 활용하니 분산 환경에서 편리합니다.', 73, 19, NULL),
('Pub/Sub 기능으로 실시간 알림 구현이 가능하네요.', 73, 32, NULL),
('자동 장애 조치와 백업 설정이 운영에 필수적이에요.', 73, 45, NULL);

-- 74번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SQS로 비동기 처리를 구현하니 시스템이 안정적이에요.', 74, 4, NULL),
('Dead Letter Queue로 실패한 메시지 처리가 편해요.', 74, 17, NULL),
('FIFO 큐와 Standard 큐 선택 기준이 명확해졌어요.', 74, 30, NULL),
('Visibility Timeout 설정이 중복 처리 방지에 중요하네요.', 74, 43, NULL),
('Lambda 트리거와 연동하면 서버리스 아키텍처가 완성되요.', 74, 10, NULL);

-- 75번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SNS로 다양한 구독자에게 동시에 알림을 보낼 수 있네요.', 75, 8, NULL),
('Fan-out 패턴으로 SQS와 함께 사용하니 효과적이에요.', 75, 21, NULL),
('메시지 필터링 정책으로 필요한 메시지만 수신할 수 있군요.', 75, 34, NULL),
('SMS, 이메일, HTTP 등 다양한 프로토콜 지원이 좋습니다.', 75, 47, NULL);

-- 76번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Cognito User Pool로 인증 시스템 구축이 간편해요.', 76, 3, NULL),
('소셜 로그인 연동이 설정만으로 가능하네요.', 76, 16, NULL),
('커스텀 Lambda 트리거로 인증 플로우를 확장할 수 있어요.', 76, 29, NULL),
('토큰 관리가 자동화되어 편리합니다.', 76, 42, NULL),
('Identity Pool과 User Pool 차이가 명확해졌어요.', 76, 11, NULL);

-- 77번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('API Gateway에서 요청 검증과 변환 기능이 강력하네요.', 77, 7, NULL),
('사용량 계획과 API 키 관리가 체계적이에요.', 77, 20, NULL),
('스테이지 변수로 환경별 배포가 쉬워졌어요.', 77, 33, NULL),
('WebSocket API 지원으로 실시간 통신도 가능하군요.', 77, 46, NULL);

-- 78번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Step Functions로 복잡한 워크플로우를 시각적으로 관리할 수 있어요.', 78, 5, NULL),
('ASL 언어로 상태 머신 정의가 직관적이네요.', 78, 18, NULL),
('에러 처리와 재시도 로직이 자동화되어 편해요.', 78, 31, NULL),
('병렬 실행과 분기 처리가 깔끔합니다.', 78, 44, NULL),
('Express Workflows로 고빈도 처리도 가능하군요.', 78, 12, NULL);

-- 79번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('ECS 서비스로 컨테이너 오케스트레이션이 편해졌어요.', 79, 9, NULL),
('Task Definition 설계 방법이 잘 정리되어 있네요.', 79, 22, NULL),
('서비스 디스커버리와 로드밸런서 연동이 자동이라 좋아요.', 79, 35, NULL),
('Auto Scaling 정책 설정으로 트래픽 대응이 유연해졌어요.', 79, 48, NULL);

-- 80번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Fargate로 서버 관리 없이 컨테이너를 실행할 수 있어서 편해요.', 80, 2, NULL),
('EC2 대비 운영 부담이 확실히 줄었네요.', 80, 15, NULL),
('비용 최적화를 위해 Spot Fargate도 활용할 수 있군요.', 80, 28, NULL),
('ECS와 Fargate 조합이 가장 실용적이에요.', 80, 41, NULL),
('리소스 사이징 가이드가 도움됐습니다.', 80, 8, NULL);

-- 81번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('VPC 서브넷 설계 패턴이 명확하게 정리되어 있어요.', 81, 6, NULL),
('퍼블릭과 프라이빗 서브넷 분리가 보안의 기본이네요.', 81, 19, NULL),
('NAT Gateway 설정으로 프라이빗 서브넷에서도 외부 통신이 가능하군요.', 81, 32, NULL),
('VPC Peering과 Transit Gateway 비교가 유용합니다.', 81, 45, NULL);

-- 82번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Security Group의 Stateful 특성을 이제 이해했어요.', 82, 4, NULL),
('인바운드와 아웃바운드 규칙 설정 가이드가 좋습니다.', 82, 17, NULL),
('최소 권한 원칙에 따른 규칙 설계가 중요하네요.', 82, 30, NULL),
('NACL과 Security Group 차이가 명확해졌어요.', 82, 43, NULL),
('보안 그룹 참조 방식으로 서비스 간 접근 제어가 깔끔합니다.', 82, 10, NULL);

-- 83번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('IAM 정책 작성 시 최소 권한 원칙이 핵심이네요.', 83, 8, NULL),
('Role 기반 접근 제어로 보안이 강화됐어요.', 83, 21, NULL),
('Cross-Account 접근 설정 방법이 실무적이에요.', 83, 34, NULL),
('IAM Policy Simulator로 권한 테스트가 가능하군요.', 83, 47, NULL);

-- 84번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('KMS로 데이터 암호화 관리가 중앙화되어 좋아요.', 84, 3, NULL),
('CMK와 AWS 관리형 키 차이가 명확해졌어요.', 84, 16, NULL),
('Envelope Encryption 개념이 잘 설명되어 있네요.', 84, 29, NULL),
('키 로테이션 자동화 설정이 보안에 필수적이에요.', 84, 42, NULL),
('S3, EBS, RDS 등 다양한 서비스와 통합이 편리합니다.', 84, 11, NULL);

-- 85번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CloudWatch 알람으로 장애 감지가 자동화됐어요.', 85, 7, NULL),
('커스텀 메트릭 전송 방법이 유용합니다.', 85, 20, NULL),
('Logs Insights로 로그 분석이 강력해졌네요.', 85, 33, NULL),
('대시보드 구성으로 한눈에 시스템 상태를 파악할 수 있어요.', 85, 46, NULL);

-- 86번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('X-Ray로 분산 시스템의 병목 지점을 쉽게 찾을 수 있어요.', 86, 5, NULL),
('서비스 맵으로 전체 아키텍처를 시각화할 수 있네요.', 86, 18, NULL),
('트레이스 샘플링 전략이 비용 절감에 중요하군요.', 86, 31, NULL),
('Lambda와 API Gateway 연동 트레이싱이 자동이라 좋아요.', 86, 44, NULL),
('커스텀 세그먼트와 어노테이션으로 세밀한 추적이 가능합니다.', 86, 12, NULL);

-- 87번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CodePipeline으로 배포 파이프라인 자동화가 완벽해요.', 87, 9, NULL),
('스테이지별 승인 프로세스 설정이 안전하네요.', 87, 22, NULL),
('GitHub 연동으로 코드 변경 시 자동 빌드가 편해요.', 87, 35, NULL),
('멀티 리전 배포 파이프라인 구성도 가능하군요.', 87, 48, NULL);

-- 88번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CodeDeploy의 Blue/Green 배포로 무중단 배포가 쉬워졌어요.', 88, 2, NULL),
('AppSpec 파일 작성법이 명확하게 설명되어 있네요.', 88, 15, NULL),
('롤백 설정으로 배포 실패 시 자동 복구가 가능해요.', 88, 28, NULL),
('배포 그룹별 전략 설정이 유연합니다.', 88, 41, NULL),
('Lambda 배포도 CodeDeploy로 관리할 수 있군요.', 88, 8, NULL);

-- 89번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Elastic Beanstalk으로 빠르게 애플리케이션을 배포할 수 있어요.', 89, 6, NULL),
('환경 설정과 스케일링이 자동이라 편하네요.', 89, 19, NULL),
('.ebextensions로 커스텀 설정이 가능하군요.', 89, 32, NULL),
('멀티 컨테이너 Docker 환경도 지원해서 좋습니다.', 89, 45, NULL);

-- 90번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Amplify로 프론트엔드 배포와 백엔드 구성이 한번에 가능해요.', 90, 4, NULL),
('CI/CD 파이프라인이 자동 생성되어 편리합니다.', 90, 17, NULL),
('GraphQL API 생성이 몇 분 만에 되네요.', 90, 30, NULL),
('인증, 스토리지, API 모듈이 통합되어 있어서 좋아요.', 90, 43, NULL),
('프론트엔드 호스팅과 도메인 설정이 간편합니다.', 90, 10, NULL);

-- 91번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('커뮤니티 이용 규칙이 명확하게 정리되어 있어서 좋네요.', 91, 8, NULL),
('신규 회원들이 참고하기 좋은 안내글이에요.', 91, 21, NULL),
('건전한 커뮤니티 문화를 위해 모두 지켜주세요.', 91, 34, NULL),
('공지 감사합니다. 숙지하겠습니다.', 91, 47, NULL);

-- 92번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('새로운 기능들이 정말 유용하네요!', 92, 3, NULL),
('알림 기능 개선이 특히 좋습니다.', 92, 16, NULL),
('UI가 더 직관적으로 변한 것 같아요.', 92, 29, NULL),
('개발팀 수고하셨습니다.', 92, 42, NULL),
('다음 업데이트도 기대됩니다!', 92, 11, NULL);

-- 93번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('이벤트 참여 방법이 간단해서 좋네요!', 93, 7, NULL),
('좋은 글 작성을 위한 동기부여가 되겠어요.', 93, 20, NULL),
('경품이 매력적이에요. 열심히 작성해야겠네요.', 93, 33, NULL),
('이벤트 기간은 언제까지인가요?', 93, 46, NULL);

-- 94번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('서버 점검 시간이 새벽이라 다행이네요.', 94, 5, NULL),
('점검 소요 시간은 얼마나 예상되나요?', 94, 18, NULL),
('사전 공지 감사합니다.', 94, 31, NULL),
('데이터 백업은 완료된 건가요?', 94, 44, NULL),
('점검 완료 후 공지도 부탁드립니다.', 94, 12, NULL);

-- 95번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('FAQ가 잘 정리되어 있어서 궁금증이 해결됐어요.', 95, 9, NULL),
('비밀번호 변경 방법을 찾고 있었는데 감사합니다.', 95, 22, NULL),
('게시글 수정과 삭제 방법도 추가해주세요.', 95, 35, NULL),
('주기적으로 업데이트해주시면 좋겠어요.', 95, 48, NULL);

-- 96번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('개인정보 처리방침 변경 내용을 확인했습니다.', 96, 2, NULL),
('데이터 보호에 신경 써주셔서 감사합니다.', 96, 15, NULL),
('제3자 제공 항목은 변경 없나요?', 96, 28, NULL),
('동의 절차가 명확해서 좋네요.', 96, 41, NULL),
('개인정보 삭제 요청은 어디서 하나요?', 96, 8, NULL);

-- 97번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('게시글 작성 가이드가 초보자에게 정말 유용하네요.', 97, 6, NULL),
('마크다운 문법 가이드도 포함되어 있어서 좋아요.', 97, 19, NULL),
('이미지 첨부 방법도 설명해주세요.', 97, 32, NULL),
('카테고리 선택 기준이 명확해졌어요.', 97, 45, NULL);

-- 98번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('불법 게시물 신고 절차가 간편하네요.', 98, 4, NULL),
('신고 처리 소요 시간은 얼마나 걸리나요?', 98, 17, NULL),
('익명 신고도 가능한가요?', 98, 30, NULL),
('건전한 커뮤니티를 위해 적극 신고하겠습니다.', 98, 43, NULL),
('신고 결과 통보도 해주시나요?', 98, 10, NULL);

-- 99번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('회원가입 이벤트 경품이 좋네요!', 99, 8, NULL),
('친구 추천하면 추가 혜택이 있나요?', 99, 21, NULL),
('이벤트 당첨자 발표는 언제인가요?', 99, 34, NULL),
('가입하길 잘했네요. 좋은 이벤트입니다!', 99, 47, NULL);

-- 100번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('이용약관 변경 내용을 꼼꼼히 읽었습니다.', 100, 3, NULL),
('주요 변경 사항 요약이 있어서 이해하기 쉬웠어요.', 100, 16, NULL),
('환불 정책 관련 조항은 변경되었나요?', 100, 29, NULL),
('법적 검토를 거친 약관이라 신뢰가 가네요.', 100, 42, NULL),
('변경 시행일이 언제인지 확인했습니다.', 100, 11, NULL);