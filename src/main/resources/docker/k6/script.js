// Exemplo do script do k6
import http from 'k6/http';
import { check, sleep } from 'k6';

export default function() {
  let res = http.get('http://localhost:8081/hello');
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
  let resp = http.get('http://localhost:8081/postagens');
  check(resp, { 'status is 200': (r) => r.status === 200 });
  sleep(2);
}
