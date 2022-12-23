CREATE TABLE "PRODUCT_COMPONENT" (
 "ID" TEXT,
 "PRODUCT_ID" TEXT NOT NULL,
 "NAME" TEXT NOT NULL,
 "DESCRIPTION" TEXT,
 FOREIGN KEY("PRODUCT_ID") REFERENCES "PRODUCT"("ID"),
 PRIMARY KEY("ID")
);
