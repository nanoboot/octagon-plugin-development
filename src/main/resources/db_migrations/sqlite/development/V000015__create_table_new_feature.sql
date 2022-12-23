CREATE TABLE "NEW_FEATURE" (
 "ID" TEXT,
 "NAME" TEXT NOT NULL UNIQUE,
 "GROUP" TEXT,
 "SINCE" TEXT,
 "DEADLINE" TEXT,
 "STATUS" TEXT,
 "RESOLUTION" TEXT,
 "RESOLUTION_COMMENT" TEXT,
 "SORTKEY" INTEGER,
 "IMPORTANCE" TEXT,
 "SIZE" TEXT,
 "NOTE" TEXT,
 "OBJECT" TEXT,
 "REMINDER_TYPE" TEXT,
 "TODO" TEXT,
 "ASAP" INTEGER,
 "REPEAT_EVERY_X_DAYS" INTEGER,
 "PROGRESS_ESTIMATION" INTEGER,
 "PARENT_ID" TEXT,
 "PRODUCT_ID" TEXT,
 "COMPONENT_ID" TEXT,
 "MODULE_ID" TEXT,
 "VERSION_ID" TEXT,
 "TARGET_MILESTONE_ID" TEXT,
 "BUDGET_YEAR" INTEGER,
 FOREIGN KEY("MODULE_ID") REFERENCES "PRODUCT_MODULE"("ID"),
 FOREIGN KEY("VERSION_ID") REFERENCES "PRODUCT_VERSION"("ID"),
 FOREIGN KEY("PRODUCT_ID") REFERENCES "PRODUCT"("ID"),
 FOREIGN KEY("COMPONENT_ID") REFERENCES "PRODUCT_COMPONENT"("ID"),
 FOREIGN KEY("TARGET_MILESTONE_ID") REFERENCES "PRODUCT_MILESTONE"("ID"),
 PRIMARY KEY("ID")
);
