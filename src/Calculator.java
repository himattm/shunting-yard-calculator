import java.util.*;

/**
 * Created by matthewcmckenna on 5/25/2016.
 */
public class Calculator {

    public static void main(String[] args) throws InvalidOperatorException {
        Calculator calculator = new Calculator();

        /*
            Test Cases:

            {"1", 1},

			{"1 + 1", 2},
			{"1 - 1", 0},
			{"1 * 1", 1},
			{"1 / 1", 1},

			{"3 + 4 + 5", 12},
			{"3 + 4 - 5", 2},
			{"3 + 4 + -5", 2},
			{"3 + 4 * 5", 23},
			{"3 * 4 / 5", 2.4m},

			{"3 + 4 + 5 * 0", 7},
			{"0 * 3 + 4 - 5", -1},

			{"1 + 1 - 1 * 1 / 1", 1},
			{"1 / 1 * 1 - 1 + 1", 1},

			{"1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1", 23},

         */

        String[] expressions = {
                "1",
                "1 + 1",
                "1 - 1",
                "1 * 1",
                "1 / 1",
                "3 + 4 + 5",
                "3 + 4 - 5",
                "3 + 4 + -5",
                "3 + 4 * 5",
                "3 * 4 / 5",
                "3 + 4 + 5 * 0",
                "0 * 3 + 4 - 5",
                "1 + 1 - 1 * 1 / 1",
                "1 / 1 * 1 - 1 + 1",
                "1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1",
        };

        String[] answers = {
                "1",
                "2",
                "0",
                "1",
                "1",
                "12",
                "2",
                "2",
                "23",
                "2.4",
                "7",
                "-1",
                "1",
                "1",
                "23"
        };

        for(int i = 0; i < expressions.length; i++) {
            String answer = calculator.evaluate(calculator.convertInfixToPostfix(expressions[i]));

            System.out.println(expressions[i] + " = " + answer +
                    "\n" + (Double.parseDouble(answers[i]) == Double.parseDouble(answer)) +
                    " " + answers[i] + " = " + answer + "\n");
        }

    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;

        Operator(int precedence) {
            this.precedence = precedence;
        }
    }

    private static Map<String, Operator> operators = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    /**
     * Checks to see if a token is a valid operator and if its precedence is greater than the one on the stack
     *
     * @param operator
     * @param sub
     * @return
     */
    private boolean isHigerPrecedence(String operator, String sub) {
        return (operators.containsKey(sub) && operators.get(sub).precedence >= operators.get(operator).precedence);
    }

    /**
     * Converts an infix expression to a postfix expression using an implementation of the Shunting-Yard Algorithm
     *
     * @param infixExp
     * @return a postfix expression String
     */
    public String convertInfixToPostfix(String infixExp) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infixExp.split("\\s")) { // Split String on spaces
            // operator
            if (operators.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrecedence(token, stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);

                // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

                // right parenthesis
            } else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.append(stack.pop()).append(' ');
                }
                stack.pop();

                // digit
            } else {
                output.append(token).append(' ');
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(' ');
        }

        return output.toString();
    }

    /**
     * Evaluates a postfix expression by pushign all digits onto the stack and when an operator is read
     * pops the required digits from the stack, evaluates, and pushes the result back to the stack.
     *
     * @param postfixExp
     * @return the result of the evaluated expression
     * @throws InvalidOperatorException
     */
    public String evaluate(String postfixExp) throws InvalidOperatorException {

        Stack<String> stack = new Stack<>();

        for (String token : postfixExp.split("\\s")) {
            // operator
            if (operators.containsKey(token)) {
                double b = Double.parseDouble(stack.pop());
                double a = Double.parseDouble(stack.pop());

                double result;

                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        result = a / b;
                        break;
                    default:
                        throw new InvalidOperatorException("The operator: " + token + " is invalid.");
                }

                // Need a workaround to ensure double safety. 3 * 4 / 5 was yielding 2.4000000000004
                stack.push(Double.toString(Math.round(result * 10000) / 10000.0));

            } else { // digit
                stack.push(token);
            }
        }

        return stack.pop();
    }

}
