package com.musigma.models;

import com.musigma.models.exception.AvantageException;
import com.musigma.models.exception.StockException;
import com.musigma.models.exception.TypeTicketException;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

import static com.musigma.utils.Log.getLogger;

/**
 * La classe Avantage représente un avantage associé à un type de ticket
 * et à un stock, avec une quantité spécifique par ticket.
 */
public class Avantage implements Serializable {

    /**
     * Logger de la classe Avantage.
     */
    private static final Logger LOGGER = getLogger(Avantage.class);
    /**
     * Type de ticket associé à cet avantage.
     */
    private final TypeTicket ticketType;
    /**
     * Stock associé à cet avantage.
     */
    private final Stock stock;
    /**
     * Quantité d'objet du stock par ticket.
     */
    private int quantityByTicket;

    /**
     * Constructeur de la classe Avantage.
     *
     * @param ticketType       le type de ticket associé à cet avantage
     * @param stock            le stock associé à cet avantage
     * @param quantityByTicket la quantité de cet avantage par ticket
     * @throws AvantageException   si la quantité par ticket est inférieure ou égale à zéro
     * @throws TypeTicketException si une exception relative au type de ticket survient
     * @throws StockException      si une exception relative au stock survient
     */
    public Avantage(TypeTicket ticketType, Stock stock, int quantityByTicket) throws AvantageException, TypeTicketException, StockException {
        LOGGER.info("Initialized Avantage");
        this.ticketType = ticketType;
        this.stock = stock;
        setQuantityByTicket(quantityByTicket);
        LOGGER.info("Created Avantage");
    }

    /**
     * Connecte cet avantage à son type de ticket et son stock.
     *
     * @throws TypeTicketException si une exception relative au type de ticket survient
     * @throws StockException      si une exception relative au stock survient
     * @throws AvantageException   si un avantage identique existe déjà pour ce stock
     */
    public void connect() throws TypeTicketException, StockException, AvantageException {
        if (ticketType.getAvantages().stream().anyMatch(av -> av.getStock().equals(stock)))
            throw new AvantageException("Un avantage identique existe déjà pour ce stock");
        ticketType.addAvantage(this);
        stock.addAvantage(this);
        LOGGER.info("Connected stock and ticket from avantage");
    }

    /**
     * Déconnecte cet avantage de son type de ticket et son stock.
     *
     * @throws TypeTicketException si une exception relative au type de ticket survient
     * @throws StockException      si une exception relative au stock survient
     */
    public void disconnect() throws TypeTicketException, StockException {
        ticketType.removeAvantage(this);
        stock.removeAvantage(this);
        LOGGER.info("Disconnected stock and ticket from avantage");
    }

    /**
     * Retourne la quantité d'objet du stock par ticket.
     *
     * @return la quantité d'objet du stock par ticket
     */
    public int getQuantityByTicket() {
        return quantityByTicket;
    }

    /**
     * Définit la quantité d'objet du stock par ticket.
     *
     * @param quantityByTicket la quantité d'objet du stock par ticket
     * @throws AvantageException si la quantité est inférieure ou égale à zéro
     */
    public void setQuantityByTicket(int quantityByTicket) throws AvantageException {
        if (quantityByTicket <= 0)
            throw new AvantageException("La quantité par ticket est inférieure ou égale à zéro, doit être positive");
        if (stock.isFixed() && quantityByTicket * ticketType.getQuantity() > stock.getQuantity())
            throw new AvantageException("La quantité par ticket multipié par le nombre de ticket surpasse les stocks");
        this.quantityByTicket = quantityByTicket;
        LOGGER.info("Set Avantage.quantityByTicket");
    }

    /**
     * Retourne le type de ticket associé à cet avantage.
     *
     * @return le type de ticket associé à cet avantage
     */
    public TypeTicket getTicketType() {
        return ticketType;
    }

    /**
     * Retourne le stock associé à cet avantage.
     *
     * @return le stock associé à cet avantage
     */
    public Stock getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        // On utilise les hashcode des ticketType et stock afin d'éviter un stackOverflow
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avantage avantage = (Avantage) o;
        return quantityByTicket == avantage.quantityByTicket &&
                Objects.equals(ticketType.hashCode(), avantage.ticketType.hashCode()) &&
                Objects.equals(stock.hashCode(), avantage.stock.hashCode());
    }

    @Override
    public int hashCode() {
        // On n'utilise pas les hashcode des avantages de ticketType et stock afin d'éviter un stackOverflow
        return Objects.hash(
                quantityByTicket,
                Objects.hash(
                        ticketType.getType(),
                        ticketType.getQuantity(),
                        ticketType.getPrice()
                ), // Pseudo hashcode pour ticketType
                Objects.hash(
                        stock.getName(),
                        stock.getQuantity(),
                        stock.isFixed()
                ) // Pseudo hashcode pour stock
        );
    }
}
