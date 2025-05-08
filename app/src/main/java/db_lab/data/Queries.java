package db_lab.data;

public final class Queries {

    public static final String TAGS_FOR_PRODUCT =
        """
            SELECT tag_name 
            FROM tagged
            WHERE product_code=?
        """;

    public static final String LIST_PRODUCTS =
        """
            SELECT code, name, description
            FROM product
        """;

    public static final String PRODUCT_COMPOSITION =
        """
            SELECT code, description, percent
            FROM composition c, material m
            WHERE c.product_code=?
            AND c.material_code=m.code
        """;

    public static final String FIND_PRODUCT =
        """
            SELECT name, description
            FROM product
            WHERE code=?
        """;
}
