#!/usr/bin/python3



def solve(clientOffers, freePremium, freeEconomy):
    return (0, 0, 0, 0)






### TESTS

testClientOffers = [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]

# (freePremium, freeEconomy, expectedPremium, premiumTotal, expectedEconomy, economyTotal)
tests = [(3, 3,    3,  738, 3, 167.99),
         (7, 5,    6, 1054, 4, 189.99),
         (2, 7,    2,  583, 4, 189.99),
         (7, 1,    7, 1153, 1,  45.99)]

def runTest(test):
    actualResult = solve(testClientOffers, test[0], test[1])
    expectedResult = test[2:]
    if actualResult == expectedResult:
        print(f"{test} ok")
    else:
        print(f"{test} FAILED, actual result: {actualResult}")

for test in tests:
    runTest(test)
