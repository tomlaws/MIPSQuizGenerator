import java.util.*;
public class MIPSQuizGenerator {
    public static class Instruction {
        private String name;
        private String format;
        private String opcode;
        private String functionCode;
        public Instruction(String name, String format, String opcode, String functionCode) {
            this.name = name;
            this.format = format;
            this.opcode = opcode;
            this.functionCode = functionCode;
        }
    }
    public static Instruction[] instructionSet = new Instruction[] {
            // arithmetic
            new Instruction("addi", "I", "001000", ""),
            //new Instruction("addiu", "I", "001001", ""),
            new Instruction("add", "R", "000000", "100000"),
            //new Instruction("addu", "R", "000000", "100001"),
            new Instruction("sub", "R", "000000", "100010"),
            //new Instruction("subu", "R", "000000", "100011"),

            // logical
            new Instruction("sll", "R", "000000", "000000"),
            new Instruction("srl", "R", "000000", "000010"),
            new Instruction("sra", "R", "000000", "000011"),
            //new Instruction("sllv", "R", "000000", "000100"),
            //new Instruction("srlv", "R", "000000", "000110"),
            //new Instruction("srav", "R", "000000", "000111"),

            new Instruction("and", "R", "000000", "100100"),
            new Instruction("or", "R", "000000", "100101"),
            new Instruction("xor", "R", "000000", "100110"),
            new Instruction("nor", "R", "000000", "100111"),

            new Instruction("andi", "I", "001100", ""),
            new Instruction("ori", "I", "001101", ""),
            new Instruction("xori", "I", "001110", ""),

            // data transfer
            //new Instruction("lui", "I", "001111", ""),
            new Instruction("lb", "I", "100000", ""),
            new Instruction("lh", "I", "100001", ""),
            new Instruction("lw", "I", "100011", ""),
            //new Instruction("lbu", "I", "100100", ""),
            //new Instruction("lhu", "I", "100101", ""),
            new Instruction("sb", "I", "101000", ""),
            new Instruction("sh", "I", "101001", ""),
            new Instruction("sw", "I", "101011", ""),

            // conditional branch
            //new Instruction("slt", "R", "000000", "101010"),
            //new Instruction("sltu", "R", "000000", "101011"),
            new Instruction("beq", "I", "000100", ""),
            new Instruction("bne", "I", "000101", ""),
    };
    public static String[][] registerSet = new String[][] {
            {"$v0", "00010"},
            {"$v1", "00011"},
            {"$a0", "00100"},
            {"$a1", "00101"},
            {"$a2", "00110"},
            {"$a3", "00111"},
            {"$t0", "01000"},
            {"$t1", "01001"},
            {"$t2", "01010"},
            {"$t3", "01011"},
            {"$t4", "01100"},
            {"$t5", "01101"},
            {"$t6", "01110"},
            {"$t7", "01111"},
            {"$s0", "10000"},
            {"$s1", "10001"},
            {"$s2", "10010"},
            {"$s3", "10011"},
            {"$s4", "10100"},
            {"$s5", "10101"},
            {"$s6", "10110"},
            {"$s7", "10111"},
            {"$t8", "11000"},
            {"$t9", "11001"},
    };
    public static class Quiz {
        private String instruction;
        private String binary;
        private String hint;
        public Quiz(String instruction, String binary, String hint) {
            this.instruction = instruction;
            this.binary = binary;
            this.hint = hint;
        }
        public boolean checkAnwser(String answer, int mode) {
            if (mode == 0)
                return this.binary.replace(" ", "").equals(answer.replace(" ", ""));
            else
                return this.instruction.replace(" ", "").equals(answer.replace(" ", ""));
        }
        public String getAnwser(int mode) {
            if (mode == 0)
                return binary;
            else
                return instruction;
        }
    }
    public static void main(String[] args) {
        Quiz qa = randomInstruction();
        Scanner sc = new Scanner(System.in);
        while (true) {
            Quiz q = randomInstruction();
            int mode = new Random().nextInt(2); // 0 : instruction to binary; 1 : binary to instruction
            if (mode == 0) {
                System.out.println("Translate the following instruction into binary number"  + " (Hint: " + q.hint + ")");
                System.out.println(q.instruction);
                //System.out.println(q.binary);
            } else {
                System.out.println("Translate the following binary number into instruction"  + " (Hint: " + q.hint + ")");
                //System.out.println(q.instruction);
                System.out.println(q.binary);
            }

            System.out.println("Please input your answer (Enter -1 to exit)");
            String s = sc.nextLine();
            if (s.equals("-1")) break;

            //
            if (q.checkAnwser(s, mode))
                System.out.println("\033[0;32mCorrect! Next move on to the next question\033[0m");
            else
                System.out.println("\033[0;31mIncorrect! The answer should be\n" + q.getAnwser(mode) + "\033[0m");
            System.out.println();
            System.out.println();
        }
        System.out.println("End.");
    }

    public static Quiz randomInstruction() {
        Instruction instruction = instructionSet[new Random().nextInt(instructionSet.length)];
        String format = instruction.format;

        // random registers
        String[] srcRegister1 = registerSet[new Random().nextInt(registerSet.length)];
        String[] srcRegister2 = registerSet[new Random().nextInt(registerSet.length)];
        String[] dstRegister = registerSet[new Random().nextInt(registerSet.length)];

        //String
        String name = instruction.name;
        String r1 = srcRegister1[0];
        String r2 = srcRegister2[0];
        String r3 = dstRegister[0];

        //Binary String
        String opcode = instruction.opcode;
        String rs = srcRegister1[1];
        String rt = srcRegister2[1];
        String rd = dstRegister[1];

        int offset = new Random().nextInt(33);
        switch (format) {
            default:
            case "I":
                String I_finalInstructionStr = "";
                String I_finalInstructionBin = "";
                // branch
                int oc = Integer.parseInt(instruction.opcode, 2);
                if (oc >= 4 && oc <= 5 || oc >= 10 && oc <= 11) {
                    I_finalInstructionStr = instruction.name + " " + r1 + "," + r3 + "," + offset;
                    I_finalInstructionBin = instruction.opcode + " " + rs + " " + rd + " " + String.format("%016d", Integer.parseInt(Integer.toString(offset, 2)));
                } else
                if (oc >= 32 && oc <= 43)
                {
                    I_finalInstructionStr = instruction.name + " " + r3 + "," + offset + "(" + r1 + ")";
                    I_finalInstructionBin = instruction.opcode + " " + rs + " " + rd + " " + String.format("%016d", Integer.parseInt(Integer.toString(offset, 2)));
                } else {
                    I_finalInstructionStr = instruction.name + " " + r3 + "," + r1 + "," + offset;
                    I_finalInstructionBin = instruction.opcode + " " + rs + " " + rd + " " + String.format("%016d", Integer.parseInt(Integer.toString(offset, 2)));
                }
                return new Quiz(I_finalInstructionStr, I_finalInstructionBin, instruction.name + " = " + Integer.toString(Integer.parseInt(instruction.opcode, 2), 16));
            case "R":
                String R_finalInstructionStr = "";
                String R_finalInstructionBin = "";

                // shift
                int func = Integer.parseInt(instruction.functionCode, 2);
                if (func >= 0 && func <= 3) {
                    int shiftAmount = new Random().nextInt(32);
                    R_finalInstructionStr = instruction.name + " " + r3 + "," + r2 + "," + shiftAmount;
                    R_finalInstructionBin = instruction.opcode + " 00000 " + rt + " " + rd + " " + String.format("%05d", Integer.parseInt(Integer.toString(shiftAmount, 2))) + " " + instruction.functionCode;
                }
                else
                if (func >= 4 && func <= 7) {
                    R_finalInstructionStr = instruction.name + " " + r3 + "," + r2 + "," + r1;
                    R_finalInstructionBin = instruction.opcode + " " + rs + " " + rt + " " + rd + " 00000 " + instruction.functionCode;
                } else {
                    R_finalInstructionStr = instruction.name + " " + r3 + "," + r1 + "," + r2;
                    R_finalInstructionBin = instruction.opcode + " " + rs + " " + rt + " " + rd + " 00000 " + instruction.functionCode;
                }
                return new Quiz(R_finalInstructionStr, R_finalInstructionBin, instruction.name + " = " + Integer.toString(Integer.parseInt(instruction.functionCode, 2), 16));
        }
    }
}
