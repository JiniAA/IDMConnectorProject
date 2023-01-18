package se.tarento.idm.generic;

import com.sap.idm.ic.DSEEntry;

import java.util.NoSuchElementException;

public class Main extends FromCustomSystem {
    public static void main(String[] args) {
        FromCustomSystem base = new FromCustomSystem();
        base.initCustom();
        boolean status=base.hasMore();
        DSEEntry entry ;
        while (status)
        {
            entry = base.getNextEntry();
            System.out.println(entry);
            status = base.hasMore();
        }
        base.exit();

    }
}