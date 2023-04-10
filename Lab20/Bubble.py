import random
import time


class Bubble:
    """Bubble class generates random data, performs a bubble sort, and prints the time taken for sorting."""

    Length = 10000

    @staticmethod
    def set_values(values):
        """
        Assigns random integer values to the provided list.

        :param values: list of integers to be filled in with random values
        """
        L = len(values)
        for i in range(L):
            values[i] = random.randint(0, 4 * L)

    @staticmethod
    def do_sort(values):
        """
        Performs an in-place bubble sort on 'values'.

        :param values: list of integers to be sorted
        """
        for i in range(len(values) - 1, 0, -1):
            for j in range(1, i + 1):
                if values[j - 1] > values[j]:
                    values[j - 1], values[j] = values[j], values[j - 1]

    @staticmethod
    def main():
        """
        Main method that generates random values, sorts them using the bubble sort algorithm,
        and prints the time taken for sorting.
        """
        values = [0] * Bubble.Length
        Bubble.set_values(values)
        start_time = time.time()
        Bubble.do_sort(values)
        end_time = time.time()
        print("{:.6f}".format(end_time - start_time))


if __name__ == "__main__":
    Bubble.main()
