package de.leanovate.dose.billing.resources;

import de.leanovate.dose.billing.connector.CartConnector;
import de.leanovate.dose.billing.connector.CustomerConnector;
import de.leanovate.dose.billing.connector.ProductConnector;
import de.leanovate.dose.billing.model.CartItem;
import de.leanovate.dose.billing.model.CartItems;
import de.leanovate.dose.billing.model.CreateOrder;
import de.leanovate.dose.billing.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    private final CartConnector cartConnector;

    private final CustomerConnector customerConnector;

    private final ProductConnector productConnector;

    public OrderResource(final CartConnector cartConnector, final CustomerConnector customerConnector,
            final ProductConnector productConnector) {

        this.cartConnector = cartConnector;
        this.customerConnector = customerConnector;

        this.productConnector = productConnector;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response prepareOrder(final CreateOrder createOrder) {

        try {
            final Customer customer = customerConnector.getCustomer(createOrder.customerId);
            final CartItems cartItems = cartConnector.getCartItems(createOrder.cartId);

            for (CartItem cartItem : cartItems.items) {
                productConnector.getProduct(cartItem.productId);
            }

            LOGGER.error("I really should create an order for " + customer + " with cart " + cartItems);
            return Response.ok().build();
        } catch (IOException e) {
            LOGGER.error("Exception", e);
            return Response.serverError().build();
        }
    }
}
