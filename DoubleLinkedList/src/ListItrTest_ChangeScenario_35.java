import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Testing for IndexedUnorderedList interface implementation: 
 * List Iterator Tests for Change Scenario 35: [A,B,C] -> remove(B) -> [A,C]
 * 
 * @author MaryJo Foster
 * @version summer 2016
 */
public class ListItrTest_ChangeScenario_35
{
	// List running tests on
	private IndexedUnorderedList<Integer> list;

	// ListIterator reference for tests 
	private ListIterator<Integer> itr; 
	
	// First element in list
	private final Integer FIRST = TestCase.A;
	// Last element in list 
	private final Integer LAST = TestCase.C;	
	// Element used in add method 
	private final Integer ADDED_ELEMENT = TestCase.E;
	// Element not in list - used for testing 
	private final Integer ELEMENT = TestCase.F;
	// Element not in list - used for negative testing 
	private final Integer INVALID_ELEMENT = TestCase.G;
		
	//********************Before Each Test Method********************
	/**
	 * Sets up list for testing: uses Parameter in XML file to select the 
	 * dynamic type of the list. 
	 * @param listType - String representing the dynamic type of the list. 
	 */
	@BeforeMethod
	@Parameters("listType")		
	public void initList(String listType)
	{
		// create empty list 
		list = TestCase.newList(listType);
		// state of list before change scenario
		list.add(TestCase.A);
		list.add(TestCase.B);
		list.add(TestCase.C);
		// the change made to the list 
		list.remove(TestCase.B);
	}
	
	/****** Tests for a new ListIterator*****************/ 
	/**
	 * Test: new ListIterator, test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: false
	 */
	@Test
	public void testListItr_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.hasPrevious(itr, false); 
	}

	/**
	 * Test: new ListIterator, test previous() - tries to return ref to previous element in the ListIterator list
	 * Expected Result: NoSuchElementException
	 */
	@Test(expectedExceptions = NoSuchElementException.class)
	public void testListItr_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.previous(itr, INVALID_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItr_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 0
	 */
	@Test
	public void testListItr_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.nextIndex(itr, 0); 
	}

	/**
	 * Test: new ListIterator, test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: -1
	 */
	@Test
	public void testListItr_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.previousIndex(itr, -1); 
	}

	/**
	 * Test: new ListIterator, test set(E) - sets element in the ListIterator list
	 * Expected Result: IllegalStateException
	 */
	@Test(expectedExceptions = IllegalStateException.class)
	public void testListItr_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.init);
		TestCase.set(itr, ELEMENT); 
	}

	/******Call add(E), then run tests******/
	/**
	 * Test: new ListIterator, call add(E); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrAdd_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.hasPrevious(itr, true); 
	}

	/**
	 * Test: new ListIterator, call add(E); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: Reference to ADD_ELEMENT (E)
	 */
	@Test
	public void testListItrAdd_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.previous(itr, ADDED_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call add(E); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrAdd_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call add(E); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 1
	 */
	@Test
	public void testListItrAdd_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.nextIndex(itr, 1); 
	}

	/**
	 * Test: new ListIterator, call add(E); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 0
	 */
	@Test
	public void testListItrAdd_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.previousIndex(itr, 0); 
	}
	
	/**
	 * Test: new ListIterator, call add(E); test set(E) - sets element in the ListIterator list
	 * Expected Result: IllegalStateException
	 */
	@Test(expectedExceptions = IllegalStateException.class)
	public void testListItrAdd_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.add);
		TestCase.set(itr, ELEMENT); 
	}
	
	/******Call next() and add(E), then run tests******/
	/**
	 * Test: new ListIterator, call next() and add(E); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrNextAdd_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.hasPrevious(itr, true); 
	}
	
	/**
	 * Test: new ListIterator, call next() and add(E); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: Reference to ADD_ELEMENT (E) 
	 */
	@Test
	public void testListItrNextAdd_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.previous(itr, ADDED_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() and add(E); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextAdd_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() and add(E); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 2
	 */
	@Test
	public void testListItrNextAdd_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.nextIndex(itr, 2); 
	}

	/**
	 * Test: new ListIterator, call next() and add(E); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 1
	 */
	@Test
	public void testListItrNextAdd_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.previousIndex(itr, 1); 
	}
	
	/**
	 * Test: new ListIterator, call next() and add(E); test set(E) - sets element in the ListIterator list
	 * Expected Result: IllegalStateException
	 */
	@Test(expectedExceptions = IllegalStateException.class)
	public void testListItrNextAdd_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextAdd);
		TestCase.set(itr, ELEMENT); 
	}

	/******Call next() and previous(), then run tests******/
	/**
	 * Test: new ListIterator, call next() and previous(); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: false
	 */
	@Test
	public void testListItrNextPrev_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.hasPrevious(itr, false); 
	}
	
	/**
	 * Test: new ListIterator, call next() and previous(); test previous() - tries to return ref to previous element in the ListIterator list
	 * Expected Result: NoSuchElementException
	 */
	@Test(expectedExceptions = NoSuchElementException.class)
	public void testListItrNextPrev_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.previous(itr, INVALID_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() and previous(); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextPrev_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() and previous(); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 0
	 */
	@Test
	public void testListItrNextPrev_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.nextIndex(itr, 0); 
	}
	
	/**
	 * Test: new ListIterator, call next() and previous(); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: -1
	 */
	@Test
	public void testListItrNextPrev_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.previousIndex(itr, -1); 
	}

	/**
	 * Test: new ListIterator, call next() and previous(); test set() - sets element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextPrev_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrev);
		TestCase.set(itr, ELEMENT); 
	}

	/********* Call next(), previous(), and add(), then run tests*********/
	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrNextPrevAdd_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.hasPrevious(itr, true); 
	}
	
	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test 
	public void testListItrNextPrevAdd_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.previous(itr, ADDED_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextPrevAdd_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 1
	 */
	@Test
	public void testListItrNextPrevAdd_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.nextIndex(itr, 1); 
	}
	
	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 0
	 */
	@Test 
	public void testListItrNextPrevAdd_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.previousIndex(itr, 0); 
	}

	/**
	 * Test: new ListIterator, call next(), previous(), and add(); test set() - sets element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextPrevAdd_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextPrevAdd);
		TestCase.add(itr, ELEMENT); 
	}

	/******** Call next() twice and previous(), then run tests **************/
	/**
	 * Test: new ListIterator, call next() twice and previous(); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrNextNextPrev_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.hasPrevious(itr, true); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice and previous(); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextPrev_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.previous(itr, FIRST); 
	}

	/**
	 * Test: new ListIterator, call next() twice and previous(); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test 
	public void testListItrNextNextPrev_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() twice and previous(); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 1
	 */
	@Test
	public void testListItrNextNextPrev_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.nextIndex(itr, 1); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice and previous(); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 0
	 */
	@Test
	public void testListItrNextNextPrev_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.previousIndex(itr, 0); 
	}

	/**
	 * Test: new ListIterator, call next() twice and previous(); test set() - sets element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test 
	public void testListItrNextNextPrev_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextPrev);
		TestCase.set(itr, ELEMENT); 
	}

	/*******Call next() twice, and add(), then run tests********/
	/**
	 * Test: new ListIterator, call next() twice, and add(); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrNextNextAdd_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.hasPrevious(itr, true); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice, and add(); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextAdd_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.previous(itr, ADDED_ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() twice, and add(); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextAdd_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() twice, and add(); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 3
	 */
	@Test
	public void testListItrNextNextAdd_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.nextIndex(itr, 3); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice, and add(); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 2
	 */
	@Test
	public void testListItrNextNextAdd_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.previousIndex(itr, 2); 
	}

	/**
	 * Test: new ListIterator, call next() twice, and add(); test set() - sets element in the ListIterator list
	 * Expected Result: IllegalStateException
	 */
	@Test(expectedExceptions = IllegalStateException.class)
	public void testListItrNextNextAdd_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAdd);
		TestCase.set(itr, ELEMENT); 
	}

	/*********Call next() twice, add(), previous(), and previous(), then run tests**********/
	/**
	 * Test: new ListIterator, call next() twice, add() and previous(); test hasPrevious() - whether there's a previous element in the ListIterator list
	 * Expected Result: true
	 */
	@Test
	public void testListItrNextNextAddPrev_hasPrevious()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.hasPrevious(itr, true); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice, add(), and previous(); test previous() - returns ref to previous element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextAddPrev_previous()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.previous(itr, LAST); 
	}

	/**
	 * Test: new ListIterator, call next() twice, add() and previous(); test add() - adds new element to the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextAddPrev_add()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.add(itr, ELEMENT); 
	}

	/**
	 * Test: new ListIterator, call next() twice, add() and previous(); test nextIndex() - index of next element in the ListIterator list
	 * Expected Result: 2
	 */
	@Test
	public void testListItrNextNextAddPrev_nextIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.nextIndex(itr, 2); 
	}
	
	/**
	 * Test: new ListIterator, call next() twice, add() and previous(); test previousIndex() - index of previous element in the ListIterator list
	 * Expected Result: 1
	 */
	@Test
	public void testListItrNextNextAddPrev_previousIndex()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.previousIndex(itr, 1); 
	}

	/**
	 * Test: new ListIterator, call next() twice, add() and previous(); test set() - sets element in the ListIterator list
	 * Expected Result: No exception
	 */
	@Test
	public void testListItrNextNextAddPrev_set()
	{
		itr = TestCase.initListItr(list, TestCase.ListItrState.nextNextAddPrev);
		TestCase.set(itr, ELEMENT); 
	}

}
