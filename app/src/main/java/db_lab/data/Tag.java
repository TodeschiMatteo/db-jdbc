package db_lab.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Tag {

    public final String name;

    public Tag(String name) {
        this.name = name == null ? "" : name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Tag) {
            var t = (Tag) other;
            return t.name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return Printer.stringify("Tag", List.of(Printer.field("name", this.name)));
    }

    public static final class DAO {

        public static Set<Tag> ofProduct(Connection connection, int productId) {
            // Iterating through a resultSet:
            // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
            var tags = new HashSet<Tag>();

            try (
                var statement = DAOUtils.prepare(connection, Queries.TAGS_FOR_PRODUCT, productId);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var tagName = resultSet.getString("tag_name");
                    var tag = new Tag(tagName);
                    tags.add(tag);
                  }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            return tags;
        }
    }
}
