package com.jamesshore.finances.ui;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.*;
import com.jamesshore.finances.domain.*;
import com.jamesshore.finances.persistence.*;
import com.jamesshore.finances.values.*;

public class _ApplicationModelTest {

	private ApplicationModel model;

	@Before
	public void setup() {
		model = new ApplicationModel();
	}

	@Test
	public void shouldStartWithDefaultStockMarket() {
		StockMarketProjection projection = model.stockMarketTableModel().stockMarketProjection();

		StockMarketYear startingYear = projection.getYearOffset(0);
		assertEquals(ApplicationModel.DEFAULT_STARTING_YEAR, startingYear.year());
		assertEquals(ApplicationModel.DEFAULT_STARTING_BALANCE, startingYear.startingBalance());
		assertEquals(ApplicationModel.DEFAULT_STARTING_COST_BASIS, startingYear.startingCostBasis());
		assertEquals(ApplicationModel.DEFAULT_GROWTH_RATE, startingYear.growthRate());
		assertEquals(ApplicationModel.DEFAULT_CAPITAL_GAINS_TAX_RATE, startingYear.capitalGainsTaxRate());
		assertEquals(ApplicationModel.DEFAULT_YEARLY_SPENDING, startingYear.totalSellOrders());

		assertEquals(41, projection.numberOfYears());
	}

	@Test
	public void shouldOnlyHaveOneInstanceOfStockMarketTableModel() {
		assertTrue("should be same instance", model.stockMarketTableModel() == model.stockMarketTableModel());
	}

	@Test
	public void changingStartingBalanceShouldChangeStockMarketTableModel() {
		model.setStartingBalance(new UserEnteredDollars("123"));
		assertEquals(new ValidDollars(123), model.stockMarketTableModel().startingBalance());
	}

	@Test
	public void changingStartingCostBasisShouldChangeStockMarketTableModel() {
		model.setStartingCostBasis(new UserEnteredDollars("39"));
		assertEquals(new ValidDollars(39), model.stockMarketTableModel().startingCostBasis());
	}

	@Test
	public void changingYearlySpendingShouldChangeStockMarketTableModel() {
		model.setYearlySpending(new UserEnteredDollars("423"));
		assertEquals(new ValidDollars(423), model.stockMarketTableModel().yearlySpending());
	}

	@Test
	public void save() throws IOException {
		class SaveFileSpy extends SaveFile {
			public boolean saveCalled = false;

			public SaveFileSpy() {
				super(null);
			}

			public void save(UserEnteredDollars startingBalance, UserEnteredDollars costBasis, UserEnteredDollars yearlySpending) {
				this.saveCalled = true;
			}
		}

		SaveFileSpy mockSaveFile = new SaveFileSpy();

		model = new ApplicationModel(mockSaveFile);
		model.save(null);
		assertTrue("saveFile.save() should have been called", mockSaveFile.saveCalled);
		// TODO: assert that the correct filename is used
	}

}
