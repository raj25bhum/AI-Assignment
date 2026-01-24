
from collections import deque

class WaterJugProblem:
    def __init__(self, jug_a_capacity, jug_b_capacity, target):
        self.jug_a_capacity = jug_a_capacity
        self.jug_b_capacity = jug_b_capacity
        self.target = target

    def get_operations(self, a, b):
        operations = []

        operations.append((self.jug_a_capacity, b, "Fill Jug A"))
        operations.append((a, self.jug_b_capacity, "Fill Jug B"))
        operations.append((0, b, "Empty Jug A"))
        operations.append((a, 0, "Empty Jug B"))

        pour_ab = min(a, self.jug_b_capacity - b)
        operations.append((a - pour_ab, b + pour_ab, "Pour A to B"))

        pour_ba = min(b, self.jug_a_capacity - a)
        operations.append((a + pour_ba, b - pour_ba, "Pour B to A"))

        return operations

    def solve_bfs(self):
        initial_state = (0, 0)
        queue = deque([(initial_state, [])])
        visited = set()
        visited.add(initial_state)

        while queue:
            (a, b), path = queue.popleft()

            if a == self.target:
                return path + [(a, b, "Goal Reached")]

            for new_a, new_b, action in self.get_operations(a, b):
                state = (new_a, new_b)
                if state not in visited:
                    visited.add(state)
                    queue.append((state, path + [(a, b, action)]))

        return None

    def display_solution(self, solution):
        if not solution:
            print("No solution found!")
            return

        
        print("WATER JUG PROBLEM SOLUTION USING BFS")

        for step, (a, b, action) in enumerate(solution):
            print(f"\nStep {step + 1}")
            print(f"Action: {action}")
            print(f"State: Jug A = {a}L, Jug B = {b}L")

            
            print(
                f"Visual: "
                f"A:[{'#' * a}{'-' * (self.jug_a_capacity - a)}] "
                f"B:[{'#' * b}{'-' * (self.jug_b_capacity - b)}]"
            )

        
        print(f"Goal achieved: Jug A contains {self.target} liters")
        print(f"Total steps: {len(solution)}")
        

    def construct_state_space_graph(self):
        
        print("STATE-SPACE GRAPH")
        

        queue = deque([(0, 0)])
        visited = set()
        visited.add((0, 0))
        graph = {}

        while queue:
            a, b = queue.popleft()
            graph[(a, b)] = []

            for new_a, new_b, action in self.get_operations(a, b):
                next_state = (new_a, new_b)
                graph[(a, b)].append((next_state, action))

                if next_state not in visited:
                    visited.add(next_state)
                    queue.append(next_state)

        print(f"\nTotal reachable states: {len(graph)}\n")

        for state in sorted(graph):
            print(f"State {state}:")
            for next_state, action in graph[state]:
                if next_state != state:
                    print(f"  -> {next_state} by {action}")
            print()

def main():
    JUG_A_CAPACITY = 4
    JUG_B_CAPACITY = 3
    TARGET = 2

    problem = WaterJugProblem(JUG_A_CAPACITY, JUG_B_CAPACITY, TARGET)

    
    print("PROBLEM FORMULATION")
    

    print("\n1. STATE REPRESENTATION:")
    print("   State = (a, b)")
    print("   a = water in Jug A")
    print("   b = water in Jug B")

    print("\n2. INITIAL STATE:")
    print("   (0, 0) - Both jugs are empty")

    print("\n3. GOAL STATE:")
    print("   (2, _) - Jug A contains exactly 2 liters")

    print("\n4. OPERATORS:")
    print("   - Fill Jug A: (a, b) -> (4, b)")
    print("   - Fill Jug B: (a, b) -> (a, 3)")
    print("   - Empty Jug A: (a, b) -> (0, b)")
    print("   - Empty Jug B: (a, b) -> (a, 0)")
    print("   - Pour A to B")
    print("   - Pour B to A")

   

    problem.construct_state_space_graph()
    solution = problem.solve_bfs()
    problem.display_solution(solution)

if __name__ == "__main__":
    main()
