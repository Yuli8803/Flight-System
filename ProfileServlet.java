import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Address;
import model.ProfileManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ManageProfile")
public class ProfileServlet extends HttpServlet {

    // Fetches all data (Addresses and Cards) and displays the page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userEmail = (session != null) ? (String) session.getAttribute("userEmail") : null;

        if (userEmail == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        ProfileManager manager = new ProfileManager();
        try {
            // Fetch both lists from the database
            List<Address> addressList = manager.getAddressesByEmail(userEmail);
            List<Object[]> cardList = manager.getCardsByEmail(userEmail);

            // Pass both lists to the JSP
            request.setAttribute("myAddresses", addressList);
            request.setAttribute("myCards", cardList);

            request.getRequestDispatcher("ManageProfile.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=db");
        }
    }

    // Handles adding, deleting, and modifying
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userEmail = (session != null) ? (String) session.getAttribute("userEmail") : null;
        String action = request.getParameter("action");
        ProfileManager manager = new ProfileManager();

        if (userEmail == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            // ADD
            if ("addAddress".equals(action)) {
                manager.addAddress(userEmail,
                        request.getParameter("street"),
                        request.getParameter("city"),
                        request.getParameter("state"),
                        request.getParameter("country"),
                        request.getParameter("zipCode"));
                response.sendRedirect("ManageProfile?success=AddressAdded");

            } else if ("addCard".equals(action)) {
                manager.addCreditCard(
                        request.getParameter("cardNumber"),
                        userEmail,
                        request.getParameter("cardHolder"),
                        request.getParameter("expDate"),
                        Integer.parseInt(request.getParameter("billingAddressId"))
                );
                response.sendRedirect("ManageProfile?success=CardAdded");

                // Modify
            } else if ("modifyAddress".equals(action)) {
                manager.updateAddress(
                        Integer.parseInt(request.getParameter("addressId")),
                        request.getParameter("street"),
                        request.getParameter("city"),
                        request.getParameter("state"),
                        request.getParameter("country"),
                        request.getParameter("zipCode"));
                response.sendRedirect("ManageProfile?success=AddressUpdated");

            } else if ("modifyCard".equals(action)) {
                manager.updateCreditCard(
                        request.getParameter("cardNumber"),
                        request.getParameter("cardHolder"),
                        request.getParameter("expDate"),
                        Integer.parseInt(request.getParameter("billingAddressId")));
                response.sendRedirect("ManageProfile?success=CardUpdated");

                //Delete
            } else if ("deleteCard".equals(action)) {
                String cardNumber = request.getParameter("cardNumber");
                manager.deleteCreditCard(cardNumber, userEmail);
                response.sendRedirect("ManageProfile?success=CardDeleted");

            } else if ("deleteAddress".equals(action)) {
                int addressId = Integer.parseInt(request.getParameter("addressId"));

                boolean deleted = manager.deleteAddress(addressId, userEmail);
                if (deleted) {
                    response.sendRedirect("ManageProfile?success=AddressDeleted");
                } else {
                    response.sendRedirect("ManageProfile?error=AddressInUse");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error handling action: " + e.getMessage());
        }
    }
}