from flask import Flask, render_template, jsonify
from collections import deque

app = Flask(__name__)

CAP_A = 4
CAP_B = 3
GOAL = 2


def get_next_states(a, b):
    states = []

    states.append((CAP_A, b, "Fill Jug A"))
    states.append((a, CAP_B, "Fill Jug B"))
    states.append((0, b, "Empty Jug A"))
    states.append((a, 0, "Empty Jug B"))

    pour = min(a, CAP_B - b)
    states.append((a - pour, b + pour, "Pour A → B"))

    pour = min(b, CAP_A - a)
    states.append((a + pour, b - pour, "Pour B → A"))

    return states


def bfs():
    start = (0, 0)

    queue = deque([start])
    visited = set([start])

    parent = {start: None}
    action = {start: None}

    goal_state = None

    while queue:
        a, b = queue.popleft()

        if a == GOAL:
            goal_state = (a, b)
            break

        for na, nb, act in get_next_states(a, b):
            if (na, nb) not in visited:
                visited.add((na, nb))
                parent[(na, nb)] = (a, b)
                action[(na, nb)] = act
                queue.append((na, nb))

    # Reconstruct path
    path = []
    actions = []

    curr = goal_state
    while curr:
        path.append(curr)
        actions.append(action[curr])
        curr = parent[curr]

    path.reverse()
    actions.reverse()

    result = []
    for i in range(len(path)):
        a, b = path[i]
        result.append({
            "a": a,
            "b": b,
            "action": actions[i] if actions[i] else "Start"
        })

    return result


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/data")
def data():
    return jsonify(bfs())


if __name__ == "__main__":
    app.run(debug=True)
