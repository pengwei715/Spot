import airflow
from airflow import DAG
from airflow.operators.bash_operator import BashOperator
from datetime import datetime, timedelta

schedule_interval = timedelta(days=2)

default_args = {
    'owner': 'Peng Wei',
    'depends_on_past': False,
    'start_date': datetime.now() - schedule_interval,
    'email': ['weipeng0715@gmail.com'],
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'scheduler',
    default_args=default_args,
    description='DAG for the Spark Batch Job',
    schedule_interval=schedule_interval)


task = BashOperator(
    task_id='run_batch_job',
    bash_command='cd /home/ubuntu/Spot/airflow ; ./spark-run.sh',
    dag=dag)


task.doc_md = """\
#### Task Documentation
Spark Batch Job is scheduled to start every other day
"""

dag.doc_md = __doc__
