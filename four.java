
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class four {

    public static Random random = new Random();
    public static int userPoints = 0;
    public static int computerPoints = 0;
    public static Scanner scan = new Scanner(System.in);
    public static int[] predictedUserNumbers = new int[3];
    public static int[] predictComputerNumbers = new int[3];
    public static int[] diceSumUserArrays = new int[3];
    public static int currentGameIndex = 0;
    public static int[] diceSumComputerArrays = new int[3];
    public static int[] userScoreForEveryTour = new int[3];
    public static int[] computerScoreForEveryTour = new int[3];
    public static int[] userPenalties = new int[3];

    public static void main(String[] args) {
        computerAndUserGame();
        scan.close();
    }

    public static void computerAndUserGame() {
        System.out.println("--- Start Game ---");

        while (currentGameIndex < 3) {
            System.out.println("\nRound: " + (currentGameIndex + 1));
            runTheGameUser();
            computerRunTheGame();
            System.out.println();
            DisplayTourScoreResult();
            userScoreForEveryTour[currentGameIndex] = userPoints;
            computerScoreForEveryTour[currentGameIndex] = computerPoints;
            currentGameIndex++;
        }

        System.out.println();
        gameFinished();

        askUserToPlay();
    }

    public static boolean computerCheated() {
        int randomNumber = random.nextInt(5) + 1;

        if (currentGameIndex + 1 == 1) {
            return randomNumber == 1;
        } else {
            if (Math.abs(computerPoints - userPoints) > 5) {
                if (Math.abs(computerPoints - userPoints) > 15) {
                    return randomNumber <= 3;
                } else {
                    return randomNumber <= 2;
                }
            } else {
                return randomNumber == 1;
            }
        }
    }

    public static boolean userCheated() {
        int randomNumber;
        switch (currentGameIndex + 1) {
            case 1:
                randomNumber = random.nextInt(2) + 1;
                return randomNumber == 2;
            case 2:
                randomNumber = random.nextInt(4) + 1;
                return randomNumber == 4;
            default:
                randomNumber = random.nextInt(6) + 1;
                return randomNumber == 6;
        }
    }

    public static void clearAllFields() {
        userPoints = 0;
        computerPoints = 0;
        currentGameIndex = 0;
        Arrays.fill(predictedUserNumbers, 0);
        Arrays.fill(predictComputerNumbers, 0);
        Arrays.fill(diceSumUserArrays, 0);
        Arrays.fill(diceSumComputerArrays, 0);
        Arrays.fill(userScoreForEveryTour, 0);
        Arrays.fill(computerScoreForEveryTour, 0);
        Arrays.fill(userPenalties, 0);
    }

    public static void askUserToPlay() {
        scan.nextLine();
        while (true) {
            System.out.printf("Do you want to play one more time (Y/N): ");
            String userAnswer = scan.nextLine();

            if ("yes".contains(userAnswer.toLowerCase())) {
                clearAllFields();
                computerAndUserGame();
            } else if ("no".contains(userAnswer.toLowerCase())) {
                System.out.println("\nProgram is Finished!");
                return;
            } else {
                System.out.println("Invalid Input!\nTry again:");
            }
        }

    }

    public static void gameFinished() {
        String symbol = "-".repeat(14);
        System.out.println(symbol + " Finish game " + symbol);
        System.out.printf("%7s | %8s %5s %10s%n", "Round", "User", "|", "Computer");

        displayLine();
        String space = " ".repeat(8);

        for (int i = 0; i < 3; i++) {
            System.out.printf("%s| Predicted: %1d | Predicted: %d%n", space, predictedUserNumbers[i], predictComputerNumbers[i]);
            System.out.printf("-  %s  - | Dice: %6d | Dice: %d%n", i + 1, diceSumUserArrays[i], diceSumComputerArrays[i]);
            System.out.printf("%s| Penalty: %3d |%n", space, userPenalties[i]);
            System.out.printf("%s| Result: %4d | Result %d%n", space, userScoreForEveryTour[i], computerScoreForEveryTour[i]);
            displayLine();
        }

        int result = arraySum(userScoreForEveryTour);
        result += arraySum(userPenalties);
        int result2 = arraySum(computerScoreForEveryTour);
    
        System.out.printf("%s| Result: %3d  | Result: %d%n", space, result, result2);
    }

    public static int arraySum(int[] array) {
        int result = 0;
        for (int i = 0; i < userScoreForEveryTour.length; i++) {
            result += userScoreForEveryTour[i];
        }
        return result;
    }

    public static void displayLine() {
        for (int i = 0; i < 41; i++) {
            if (i == 8 || i == 23) {
                System.out.print("+");
                continue;
            }
            System.out.print("-");
        }
        System.out.println();
    }

    public static void computerRunTheGame() {
        int computerPredictNumber = computerPredictNumber();
        predictComputerNumbers[currentGameIndex] = computerPredictNumber;
        System.out.printf("Computer predicted %d points%n", computerPredictNumber);
        System.out.println("Computer rolls the dices...");

        int cub1Number = rollTheDice();
        int cub2Number = rollTheDice();

        if (computerCheated()) {
            System.out.println("Computer successfully cheated!");
            if (computerPredictNumber % 2 == 0) {
                cub1Number = computerPredictNumber / 2;
                cub2Number = computerPredictNumber / 2;
            } else {
                cub1Number = computerPredictNumber / 2 + 1;
                cub2Number = computerPredictNumber / 2;
            }
        }

        printDice(cub1Number);
        printDice(cub2Number);

        int sumDices = cub1Number + cub2Number;
        diceSumComputerArrays[currentGameIndex] = sumDices;

        computerPoints += findTotalPoints(sumDices, computerPredictNumber);

        System.out.printf("On the Dice fell %d points%n", sumDices);
        System.out.printf("Result is %3d - abs(%d - %d) * 2: %d points%n", sumDices, sumDices, computerPredictNumber, computerPoints);
    }

    public static void runTheGameUser() {
        int userPredictNumber = userPredictsNumber();
        predictedUserNumbers[currentGameIndex] = userPredictNumber;

        int cub1Number = rollTheDice();
        int cub2Number = rollTheDice();

        scan.nextLine();
        System.out.print("Do you want to cheat? {Y/N}: ");
        String userInput = scan.nextLine();

        if ("y".equalsIgnoreCase(userInput)) {
            if (userCheated()) {
                System.out.println("User cheated successfully");
                if (userPredictNumber % 2 == 0) {
                    cub1Number = userPredictNumber / 2;
                    cub2Number = userPredictNumber / 2;
                } else {
                    cub1Number = userPredictNumber / 2 + 1;
                    cub2Number = userPredictNumber / 2;
                }
            } else {
                System.out.println("User got penalty: -10!");
                userPenalties[currentGameIndex] = -10;
            }
        }

        printDice(cub1Number);
        printDice(cub2Number);

        int sumDices = cub1Number + cub2Number;
        diceSumUserArrays[currentGameIndex] = sumDices;

        userPoints = findTotalPoints(sumDices, userPredictNumber);

        System.out.printf("On the Dice fell %d points%n", sumDices);
        System.out.printf("Result is %d - abs(%d - %d) * 2: %d points%n", sumDices, sumDices, userPredictNumber, userPoints);
    }

    public static void DisplayTourScoreResult() {
        System.out.println("---------- Current Score ----------");
        System.out.printf("User: %d points%n", userPoints);
        System.out.printf("Computer: %d points%n", computerPoints);

        if (userPoints > computerPoints) {
            System.out.printf("User is ahead by %d points!%n", userPoints);
        } else if (userPoints == computerPoints) {
            System.out.printf("tie!%n", computerPoints);
        } else {
            System.out.printf("Computer is ahead by %d points!%n", computerPoints);
        }
        System.out.println("-".repeat(35));
    }

    public static int userPredictsNumber() {
        while (true) {
            System.out.print("Enter number (2, 12): ");
            int userNumber = scan.nextInt();

            if (userNumber >= 2 && userNumber <= 12) {
                return userNumber;
            }
        }
    }

    public static int computerPredictNumber() {
        return random.nextInt(11) + 2;
    }

    public static int rollTheDice() {
        int cube = random.nextInt(6) + 1;
        return cube;
    }

    public static void printDice(int cubeNumber) {
        String cubeUpperSide = "-".repeat(10);
        cubeUpperSide = "+".concat(cubeUpperSide).concat("+");

        String[][] arrayOfCubeNumbers = designCubeToDisplay(cubeNumber);

        System.out.println(cubeUpperSide);
        for (String[] arrayOfCubeNumber : arrayOfCubeNumbers) {
            for (String arrayOfCubeNumber1 : arrayOfCubeNumber) {
                System.out.print(arrayOfCubeNumber1);
            }
            System.out.println();
        }
        System.out.println(cubeUpperSide + "\n");
    }

    public static String[][] designCubeToDisplay(int cubeNumber) {
        String[][] arrayOfCubeNumbers = new String[5][12];

        for (String[] arrayOfCubeNumber : arrayOfCubeNumbers) {
            Arrays.fill(arrayOfCubeNumber, " ");
        }

        for (String[] arrayOfCubeNumber : arrayOfCubeNumbers) {
            arrayOfCubeNumber[0] = "|";
            arrayOfCubeNumber[arrayOfCubeNumber.length - 1] = "|";
        }

        switch (cubeNumber) {
            case 1:
                arrayOfCubeNumbers[2][5] = "#";
                break;
            case 2:
                arrayOfCubeNumbers[0][2] = "#";
                arrayOfCubeNumbers[arrayOfCubeNumbers.length - 1][arrayOfCubeNumbers[0].length - 3] = "#";
                break;
            case 3:
                arrayOfCubeNumbers[0][2] = "#";
                arrayOfCubeNumbers[2][5] = "#";
                arrayOfCubeNumbers[4][arrayOfCubeNumbers[0].length - 3] = "#";
                break;
            case 4:
                arrayOfCubeNumbers[0][2] = "#";
                arrayOfCubeNumbers[0][arrayOfCubeNumbers[0].length - 3] = "#";
                arrayOfCubeNumbers[4][2] = "#";
                arrayOfCubeNumbers[4][arrayOfCubeNumbers[0].length - 3] = "#";
                break;
            case 5:
                arrayOfCubeNumbers[0][2] = "#";
                arrayOfCubeNumbers[0][arrayOfCubeNumbers[0].length - 3] = "#";
                arrayOfCubeNumbers[2][5] = "#";
                arrayOfCubeNumbers[4][2] = "#";
                arrayOfCubeNumbers[4][arrayOfCubeNumbers[0].length - 3] = "#";
                break;
            case 6:
                arrayOfCubeNumbers[0][2] = "#";
                arrayOfCubeNumbers[0][arrayOfCubeNumbers[0].length - 3] = "#";
                arrayOfCubeNumbers[2][2] = "#";
                arrayOfCubeNumbers[2][arrayOfCubeNumbers[0].length - 3] = "#";
                arrayOfCubeNumbers[4][2] = "#";
                arrayOfCubeNumbers[4][arrayOfCubeNumbers[0].length - 3] = "#";
        }
        return arrayOfCubeNumbers;
    }

    public static int findTotalPoints(int cubSum, int userNumber) {
        return cubSum - Math.abs(cubSum - userNumber) * 2;
    }
}
