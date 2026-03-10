# Homeworks from Parallel Algorithms 1

## Instructions:

Each directory contains a solution in the python programming language along with a reference entry, input data and execution logs.

## Executions

```bash
python -m venv .venv
source .venv/bin/activate 
pip install -r requirements.txt
```

Each solution has simple CLI arguments with `--help` option

## Examples of running individual tasks

### U1
```bash
python solution.py input.txt -c 16 -s
```

### U2
```bash
python solution.py input.csv -c 16 -r 100 -i 300 -ic
```

### U3
```bash
python solution.py input.txt.gz -c 16 -r 500000 -i 100 -ic  
```