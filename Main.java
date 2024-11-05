import java.util.*;

class Opcode {
    String mnemonic;
    int opcode;

    Opcode(String mnemonic, int opcode) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
    }
}

class SymbolTable {
    private Map<String, Integer> table = new HashMap<>();

    public void add(String symbol, int address) {
        table.put(symbol, address);
    }

    public Integer get(String symbol) {
        return table.get(symbol);
    }
}

class PoolTable {
    private List<String> literals = new ArrayList<>();

    public void add(String literal) {
        literals.add(literal);
    }

    public List<String> getLiterals() {
        return literals;
    }
}

class MacroProcessor {
    private List<Opcode> opcodeTable;
    private SymbolTable symbolTable;
    private PoolTable poolTable;

    public MacroProcessor() {
        opcodeTable = new ArrayList<>();
        symbolTable = new SymbolTable();
        poolTable = new PoolTable();
        initializeOpcodeTable();
    }

    private void initializeOpcodeTable() {
        opcodeTable.add(new Opcode("ADD", 1));
        opcodeTable.add(new Opcode("SUB", 2));
        opcodeTable.add(new Opcode("MUL", 3));
        opcodeTable.add(new Opcode("DIV", 4));
        opcodeTable.add(new Opcode("MOV", 5));
        // Add more opcodes as needed
    }

    public void passTwo(List<String> assemblyCode) {
        int addressCounter = 0;

        for (String line : assemblyCode) {
            String[] tokens = line.split("\\s+");
            String instruction = tokens[0];

            if (instruction.equals("END")) {
                break;
            }

            Integer opcode = getOpcode(instruction);
            if (opcode != null) {
                System.out.print(addressCounter + ": " + opcode);
                for (int i = 1; i < tokens.length; i++) {
                    String token = tokens[i];
                    Integer symbolAddress = symbolTable.get(token);
                    if (symbolAddress != null) {
                        System.out.print(" " + symbolAddress);
                    } else {
                        poolTable.add(token);
                    }
                }
                System.out.println();
                addressCounter++;
            }
        }

        // Print pool table
        System.out.println("Pool Table: " + poolTable.getLiterals());
    }

    private Integer getOpcode(String mnemonic) {
        for (Opcode opcode : opcodeTable) {
            if (opcode.mnemonic.equals(mnemonic)) {
                return opcode.opcode;
            }
        }
        return null; // Not found
    }

    public void addSymbol(String symbol, int address) {
        symbolTable.add(symbol, address);
    }
}

public class Main {
    public static void main(String[] args) {
        MacroProcessor processor = new MacroProcessor();

        // Simulating Pass-I: Adding symbols
        processor.addSymbol("START", 0);
        processor.addSymbol("LOOP", 1);
        processor.addSymbol("END", 2);

        // Simulated assembly code
        List<String> assemblyCode = Arrays.asList(
                "START",
                "MOV A, B",
                "ADD C",
                "SUB D",
                "END"
        );

        // Execute Pass-II
        processor.passTwo(assemblyCode);
    }
}
