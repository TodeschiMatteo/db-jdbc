package db_lab.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ProductPreview {

    public final int code;
    public final String name;
    public final Set<Tag> tags;

    public ProductPreview(int code, String name, Set<Tag> tags) {
        this.code = code;
        this.name = name == null ? "" : name;
        this.tags = tags == null ? Set.of() : Collections.unmodifiableSet(new HashSet<>(tags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof ProductPreview) {
            var p = (ProductPreview) other;
            return p.code == this.code && p.name.equals(this.name) && p.tags.equals(this.tags);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.name, this.tags);
    }

    @Override
    public String toString() {
        return Printer.stringify(
            "ProductPreview",
            List.of(
                Printer.field("code", this.code),
                Printer.field("name", this.name),
                Printer.field("tags", this.tags)
            )
        );
    }

    public static final class DAO {

        public static List<ProductPreview> list(Connection connection) {
            // Iterating through a resultSet:
            // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
            var previews = new ArrayList<ProductPreview>();

            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_PRODUCTS);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    // To read columns from the curren row we will use the `getX` methods
                    // of the result set.
                    var code = resultSet.getInt("code");
                    var name = resultSet.getString("name");
                    // Notice how the work of fetching a product's tags is carried out by
                    // another DAO!
                    var tags = Tag.DAO.ofProduct(connection, code);
                    var preview = new ProductPreview(code, name, tags);
                    previews.add(preview);
                  }
            } catch (SQLException e) {
                // We just wrap the checked SQLException in an unchecked one so that
                // it won't bubble up in all the method's signatures.
                throw new DAOException(e);
            }

            return previews;
        }
    }
}
