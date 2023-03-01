import time
import uuid
from locust import HttpUser, task, constant_throughput, events

class LoadtestUser(HttpUser):
    wait_time = constant_throughput(1)
    fixed_count = 4096

    @task(2)
    def get_session(self):
        with self.client.get(f"/api/v1/find-call-session?key={uuid.uuid4()}", name="/api/v1/find-call-session", catch_response=True) as response:
            if response.status_code == 404:
                response.success()

    @events.test_start.add_listener
    def on_test_start(environment, **kwargs):
        print("Load test begins")

    @events.test_stop.add_listener
    def on_test_stop(environment, **kwargs):
        print("Load test ends")