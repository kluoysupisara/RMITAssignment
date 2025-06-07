import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import model.*;
import dao.ShoppingCartDao;
import org.junit.Before;
import org.junit.Test;

public class ShoppingCartTest {

    private ShoppingCart cart;
    private Event event1;
    private Event event2;

    // Dummy implementation of ShoppingCartDao for test
    private static class DummyCartDao implements ShoppingCartDao {
        @Override
        public void saveOrUpdateItem(String username, int eventId, int quantity) {
            // no-op
        }

        @Override
        public void removeItem(String username, int eventId) {
            // no-op
        }

        @Override
        public void clearCart(String username) {
            // no-op
        }

        @Override
        public void createTable() throws SQLException {
            // no-op
        }

        @Override
        public List<CartItems> loadCartItems(String username) {
            return null; // not needed in this test
        }
    }

    @Before
    public void setUp() {
        cart = new ShoppingCart("testuser", new DummyCartDao());
        event1 = new Event(1, "Jazz Night", "Studio A", "Fri", 50.0, 0, 100, 100, true);
        event2 = new Event(2, "Rock Show", "Studio B", "Sat", 60.0, 0, 80, 80, true);
    }

    @Test
    public void testAddNewItemToCart() throws SQLException {
        cart.addItem(event1, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testUpdateExistingItemQuantity() throws SQLException {
        cart.addItem(event1, 2);
        cart.addItem(event1, 3); // should update quantity to 5
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddMultipleItems() throws SQLException {
        cart.addItem(event1, 1);
        cart.addItem(event2, 2);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    public void testRemoveItemFromCart() throws SQLException {
        cart.addItem(event1, 2);
        cart.removeItem(event1);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testClearCart() throws SQLException {
        cart.addItem(event1, 2);
        cart.addItem(event2, 3);
        cart.clear();
        assertTrue(cart.getItems().isEmpty());
    }
}
