#!/usr/bin/python3

# In the following informal picture we present the ranges of client
# offers and their fullfillment. Ranges are marked with square
# brackets and they include each other as determined by their
# horizontal position.  If one bracket is directly above another, we
# mean they directly coincide; on one line the same is shown with a
# vertical line. Otherwise, Ellipsis means that it ends at the first
# dot or farther to the right and it doesn't matter exactly.
# The notation "[-----[" means that the range starts somewhere within
# those limits.
#
# Everything is sorted by decreasing value from left to right.
#
# Given arguments are in capital letters, things to be calculated are
# in small letters.
#
# HIGH OFFERS  - client offers >= 100
# LOW OFFERS   - client offers < 100
# FREE PREMIUM - free Premium rooms
# FREE ECONOMY - free Economy rooms
# premium left - Premium rooms not booked for >= 100
# economy left - Economy rooms not booked
# high taken   - rooms booked to clients who offered >= 100 (always Premium)
# low upgraded - Premium rooms booked to clients who offered < 100
# low taken    - Economy rooms booked to clients who offered < 100
#
# Picture:
#
#   [ FREE PREMIUM                     ] [ FREE ECONOMY             ]
#   [ HIGH OFFERS ...
#   [ high taken  | premium left       ]
#            [----------[ LOW OFFERS                 ]
#                 [-----[ low upgraded ] [ low taken | economy left ] 
#
# Additional constraint: #(low upgraded) > 0 implies #(economy left) == 0
#
# From this picture we can see that a straightforward algorithm will follow these lines:
# 1) determine the counts:
#   1.1) determine `high taken`, which also gives us `premium left`,
#   1.2) determine `low taken`, which also gives us `economy left`,
#   1.3) now we determine `low upgraded` as it depends on `premium left` and `economy left`.
# 2) select the customers:
#   1) high taken - the top paying
#   2) low upgraded - the top from < 100
#   3) low taken - the top from remaining < 100


def solve(clientOffers, freePremium, freeEconomy):
    sortedOffers = sorted(clientOffers, reverse=True)
    highOffers = [offer for offer in sortedOffers if offer >= 100]
    lowOffers =  [offer for offer in sortedOffers if offer < 100]

    highOffersCountTaken = min(len(highOffers), freePremium)
    lowOffersCountTaken = min(len(lowOffers), freeEconomy)
    upgradedLowOffersCountTaken = min(len(lowOffers) - lowOffersCountTaken,
                                      freePremium - highOffersCountTaken)

    highOffersTaken = highOffers[0:highOffersCountTaken]
    upgradedLowOffersTaken = lowOffers[0:upgradedLowOffersCountTaken]
    lowOffersTaken = lowOffers[upgradedLowOffersCountTaken:
                               (upgradedLowOffersCountTaken + lowOffersCountTaken)]

    return (highOffersCountTaken+upgradedLowOffersCountTaken,
            sum(highOffersTaken)+sum(upgradedLowOffersTaken),
            lowOffersCountTaken,
            sum(lowOffersTaken))


### TESTS

testClientOffers = [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]

# (freePremium, freeEconomy, expectedPremium, premiumTotal, expectedEconomy, economyTotal)
tests = [(3, 3,    3,  738,    3, 167.99),
         (7, 5,    6, 1054,    4, 189.99),
         (2, 7,    2,  583,    4, 189.99),
         (7, 1,    7, 1153.99, 1,  45)]

def runTest(test):
    actualResult = solve(testClientOffers, test[0], test[1])
    expectedResult = test[2:]
    if actualResult == expectedResult:
        print(f"{test} ok")
    else:
        print(f"{test} FAILED, actual result: {actualResult}")

for test in tests:
    runTest(test)
