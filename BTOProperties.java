import java.util.ArrayList;

public class BTOProperties {
    String projectName;
    String neighbourhood;
    String firstVariant; //2 Room / 3 Room
    int firstVariantAmt;
    int firstVariantPrice; //Float Double?
    String secondVariant; 
    int secondVariantAmt;
    int secondVariantPrice;
    String openDate;
    String closeDate; //Maybe import module for dates?
    ArrayList<Manager> managerIC;
    int officerSlot;
    ArrayList<Officer> officers;
    

    public BTOProperties(String projName, String neighbourhood, String firstVariant,
    int firstVariantAmt, int firstVariantPrice, String secondVariant, int secondVariantAmt,
    int secondVariantPrice, String openDate, String closeDate, ArrayList<Manager> managerICRef, int officerSlot, ArrayList<Officer> officerList){
        
        this.projectName = projName;
        this.neighbourhood = neighbourhood;
        this.firstVariant = firstVariant;
        this.firstVariantAmt = firstVariantAmt;
        this.firstVariantPrice = firstVariantPrice;
        this.secondVariant = secondVariant;
        this.secondVariantAmt = secondVariantAmt;
        this.secondVariantPrice = secondVariantPrice;
        this.openDate = openDate;
        this.closeDate = closeDate;
        managerIC = managerICRef;
        this.officerSlot = officerSlot;
        officers = officerList; //Holds Object references to the officers

    }

    public ArrayList<Officer> getOfficers(){
        return officers;
    }

    public void allInfo(){ //Troubleshooting
        System.out.printf("Project Name: %s Neighbourhood: %s\n"
        + "firstVar: %s firstVarAmt: %d firstVarPrice: %d\n"
        + "secondVar: %s secondVarAmt: %d secondVarPrice: %d\n"
        + "openDate: %s closeDate: %s Officer Slot: %d\n"
        ,projectName,neighbourhood,firstVariant,firstVariantAmt,firstVariantPrice,secondVariant,secondVariantAmt,secondVariantPrice,openDate,closeDate,officerSlot);

        System.out.println("Managers Involved:");
        for(Manager m: managerIC){
            System.out.println(m.getName());
        }

        System.out.println("Officers Involved:");
        for(Officer o : officers){
            System.out.println(o.getName());
        }

    }

}
