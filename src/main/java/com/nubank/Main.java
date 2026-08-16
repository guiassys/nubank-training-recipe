package com.nubank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String SYMBOL = "*";
    private static final String SPACE = " ";
    private static final int TOTAL_LINES = 10;
    private static final int TOTAL_SYMBOLS_BY_LINE = 20;

    public static void main(String[] args) {

        // Printing one squad
        logger.info("\n\n ------ SQUAD ------");
        int index = 1;
        while(index <= TOTAL_LINES){
            logger.info(SYMBOL.repeat(TOTAL_SYMBOLS_BY_LINE));
            index++;
        }

        // Printing one triangle
        logger.info("\n\n ----- TRIANGLE -----");
        index = 1;
        while(index <= (TOTAL_LINES*2)){
            boolean isImparNumber = index % 2 != 0;

            if(isImparNumber){
                int spaces = (TOTAL_SYMBOLS_BY_LINE - index) /2;
                logger.info(SPACE.repeat(spaces) + SYMBOL.repeat(index) + SPACE.repeat(spaces));
            }

            index++;
        }

        logger.info("\n\n -- Nubank interview training - Recipe rating system --");

    }
}
