Feature: Patient Registration

  @PatientRegistration @Regression
  Scenario: Register the patient in oasis application
    #1.1 Patient Registration
    Given User navigates to Oasis app URL
    When I click on "HamburgerMenu" link
    Then I click on "TVAppliancesElectronics" link
    And I click on "Televisions" link
    And I click on "SmartTelevisions" link
    And I click on "SamsungBrand" link
    And I click on "SortBy" link
    And I click on "HighToLow" link
    And I click on "SecondHighest" link
    And I switch to nexttab 
    And I verify "AboutThisItem" is Present
    And I log the "ItemDescription" value to console
    
    
    
   
    

