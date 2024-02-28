#!/usr/bin/env python
import os
import sys
import argparse
import multiprocessing
import importlib
import signal
import socket


if "" not in sys.path:
    sys.path.insert(0, "")


class WSGIWorkerManager:
    def __init__(self, host: str, port: int, workers: int, threads: int, app_import_string: str):
        self.host = host
        self.port = port
        self.workers = workers
        self.threads = threads
        self.worker_processes = []
        self.app_import_string = app_import_string

    def __import(self, app_import_string: str):
        module_name, app_name = app_import_string.rsplit(':', 1)
        module = importlib.import_module(module_name)
        return getattr(module, app_name)

    def serve_cheroot(self):
        from cheroot import wsgi
        app = self.__import(self.app_import_string)
        server = wsgi.Server((self.host, self.port), app, self.threads, reuse_port=True, request_queue_size=4096)
        print(f"Serving on {self.host}:{self.port} with PID {os.getpid()} with {self.threads} threads")
        server.safe_start()
    
    def worker_process(self):
        self.serve_cheroot()

    def start_workers(self):
        if not hasattr(socket, "SO_REUSEPORT"):
            raise ValueError("SO_REUSEPORT is not supported on this platform. Cannot start multiple workers.")

        for _ in range(self.workers):
            p = multiprocessing.Process(target=self.worker_process)
            p.start()
            self.worker_processes.append(p)

    def terminate_workers(self, signal_received, frame):
        for p in self.worker_processes:
            print(f"Terminating process {p.pid}")
            p.terminate()
        
        for p in self.worker_processes:
            p.join()
        
        print("All worker processes terminated. Exiting.")
        os._exit(0)

    def run(self):
        signal.signal(signal.SIGINT, self.terminate_workers)
        self.start_workers()
        try:
            for p in self.worker_processes:
                p.join()
        except KeyboardInterrupt:
            self.terminate_workers(None, None)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Load and serve a WSGI application with bjoern.")
    parser.add_argument('--host', type=str, default="0.0.0.0", help="Host for the WSGI application.")
    parser.add_argument('--port', type=int, default=3000, help="Port for the WSGI application.")
    parser.add_argument('--workers', type=int, default=2, help="Number of worker processes.")
    parser.add_argument('--threads', type=int, default=4, help="Number of threads per worker processes.")
    parser.add_argument('app_import_string', type=str, help="The WSGI application import string, e.g., 'path.to.app:app'.")

    args = parser.parse_args()
    
    manager = WSGIWorkerManager(args.host, args.port, args.workers, args.threads, args.app_import_string)
    manager.run()
