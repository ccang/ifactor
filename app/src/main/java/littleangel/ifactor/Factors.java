package littleangel.ifactor;

import java.util.ArrayList;
import java.util.Collections;

public class Factors {
    private long number;
    private ArrayList<Long> sciNotation = new ArrayList<Long>();
    private ArrayList<Long> distinctFactor = new ArrayList<Long>();
    private double runningTime, buildingTime; // in seconds: time for factorization and time for building factor list

    // Do Factorization when the object is created
    Factors(long number) {
        if (number < 2) {
            throw new IllegalArgumentException ("Number should be >=2");
        }
        this.number = number;
        this.runningTime = factorize(number,this.sciNotation);
        long startTime = System.currentTimeMillis();
        findFactors(this.sciNotation,this.distinctFactor,0,1);
        Collections.sort(this.distinctFactor);
        long endTime = System.currentTimeMillis();
        this.buildingTime = (endTime - startTime) / 1000.0;
    }

    // Deep copy constructor
    Factors(Factors arg) {
        this.number = arg.number;
        this.runningTime = arg.runningTime;
        this.buildingTime = arg.buildingTime;
        this.sciNotation.clear();
        this.distinctFactor.clear();
        for(Long l :  arg.sciNotation) { this.sciNotation.add(l); }
        for(Long l :  arg.distinctFactor) { this.distinctFactor.add(l); }
    }

    // Getters just in case
    public long getNumber() {
        return number;
    }
    public ArrayList<Long> getSciNotation() {
        return sciNotation;
    }
    public ArrayList<Long> getDistinctFactor() {
        return distinctFactor;
    }
    // Getters actually in use
    public double getRunningTime() {
        return runningTime;
    }
    public double getBuildingTime() {
        return buildingTime;
    }

    public int getTotalFactor() {
        int tmp = 1;
        for (int i = 1; i < (this.sciNotation.size()); i+=2) {
            tmp *= (this.sciNotation.get(i) + 1);
        }
        return tmp;
    }

    // Format the sciNotation
    public String getFormatNotation(boolean machine) {
        String tmp = "" + number;
        if ((this.sciNotation.size() == 2) && (this.sciNotation.get(1)==1)) {
            tmp += " is prime";
        } else {
            tmp += " = ";
            for (int i = 0; i < (this.sciNotation.size() - 1); i+=2) {
                tmp = tmp + this.sciNotation.get(i);
                if (machine) {
                    if (this.sciNotation.get(i+1) > 1) { tmp = tmp + "^" + this.sciNotation.get(i+1); }
                    if (i != this.sciNotation.size() - 2) {tmp = tmp + " * ";}
                } else {
                    if (this.sciNotation.get(i + 1) > 1) {tmp = tmp + "<sup><small>" + this.sciNotation.get(i + 1) + "</small></sup>";  }
                    if (i != this.sciNotation.size() - 2) {tmp = tmp + " x ";}
                }
            }
        }
        return tmp;
    }

    // format the distinctFactors
    public String getFormatDistinctFactor(String delimiter) {
        String tmpStr="";
        for (int i=0; i<this.distinctFactor.size()-1; i++) {
            tmpStr = tmpStr + this.distinctFactor.get(i) + delimiter;
        }
        tmpStr = tmpStr + this.distinctFactor.get(this.distinctFactor.size() - 1);
        return tmpStr;
    }

    // calculate all factors
    private void findFactors(ArrayList<Long> input, ArrayList<Long> output, int currentDivisor, long currentResult) {
        if (currentDivisor == input.size()) {
            // no more balls
            output.add(currentResult);
            return;
        }
        for (int i = 0; i <= input.get(currentDivisor+1); i++) {
            findFactors(input, output, currentDivisor+2, currentResult);
            currentResult *= input.get(currentDivisor);
        }
    }

    // factorize the input number
    private double factorize(long input, ArrayList<Long> inputArray) {
        ArrayList<Long> flatNotation = new ArrayList<Long>();
        long last = -1;

        long startTime = System.currentTimeMillis();
        // Generate the interval wheel
        ArrayList<Integer> coprime = new ArrayList <Integer>();
        ArrayList<Integer> interval = new ArrayList <Integer>();
        for (int i=1; i<2*3*5*7*11; i++) {
            if (i%2!=0 && i%3!=0 && i%5!=0 && i%7!=0 && i%11!=0) {
                coprime.add(i);
            }
        }
        for (int i=0; i<coprime.size()-1; i++) {
            interval.add(coprime.get(i+1)-coprime.get(i));
        }
        int period=interval.size();
        int flag=1;

        // factorize
        long trying = 2;
        while (trying*trying <= input) {
            if (input % trying == 0) {
                input /= trying;
                flatNotation.add(trying);
            }
            else {
                if (trying == 2) { trying = 3; }
                else if (trying == 3) { trying = 5; }
                else if (trying == 5) { trying = 7; }
                else if (trying == 7) { trying = 11; }
                else if (trying == 11) { trying = 13; }
                else {
                    trying += interval.get(flag);
                    flag ++;
                    if (flag == period) {flag = 0;}
                }
            }
        }
        flatNotation.add(input);
        long endTime = System.currentTimeMillis();

        // return science notation
        inputArray.clear();
        for (int i = 0; i < (flatNotation.size()); i++) {
            long tmp1 = flatNotation.get(i);
            if (tmp1 != last) {
                last = tmp1;
                inputArray.add(tmp1);
                inputArray.add(Long.valueOf(1));
            }
            else {
                inputArray.set(inputArray.size()-1, inputArray.get(inputArray.size()-1) + 1);
            }
        }

        // return runtime
        return ((endTime - startTime) / 1000.0);
    }
}
