#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int P8_array[] = {6, 3, 7, 4, 8, 5, 10, 9};
int initial_permutation[] = {2, 6, 3, 1, 4, 8, 5, 7};
int final_permutation[] = {4, 1, 3, 5, 7, 2, 8, 6};

char *initial_permuttation(char *bits)
{
  int P10_array[] = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
  char *aux = (char *)malloc(10 * sizeof(char));

  int i = 0;
  for (i; i < 10; i++)
  {
    aux[i] = bits[P10_array[i] - 1];
  }

  return aux;
}

char *p8_permuttation(char *bits)
{
  int *indexes = P8_array;
  char *aux = (char *)malloc(8 * sizeof(char));

  int i = 0;
  for (i; i < 8; i++)
  {
    aux[i] = bits[indexes[i] - 1];
  }

  return aux;
}

char *permuttation(char *bits, int *bits_indexes, int size)
{
  int i = 0;

  char *aux = (char *)malloc(size * sizeof(char));

  for (i; i < size; i++)
    aux[i] = bits[bits_indexes[i] - 1];

  return aux;
}

char *separation(char *bits, int start_index, int size_of_string)
{
  char *aux = (char *)malloc(size_of_string * sizeof(char));
  const void *src = &bits[start_index];
  return memcpy(aux, src, size_of_string);
}

char *exec_rotation_left(char *bits, int shift_position, int size)
{
  int shifted_bits[shift_position];
  char *aux = (char *)malloc(size * sizeof(char));

  int i = 0;
  for (i = 0; i < shift_position; i++)
  {
    shifted_bits[i] = bits[i];
  }

  int j = 0;
  for (j = 0; j < size - shift_position; j++)
  {
    aux[j] = bits[j + shift_position];
  }

  int k = 0;
  int l = 0;
  for (k = size - shift_position; k < size; k++)
  {
    aux[k] = shifted_bits[l++];
  }

  return aux;
}

char *exec_rotation_right(char *bits, int shift_position, int size)
{
  int shifted_bits[shift_position];
  char *aux = (char *)malloc(size * sizeof(char));

  int i = 0;
  for (i = 0; i < shift_position; i++)
  {
    shifted_bits[i] = bits[i];
  }

  int j = 0;
  for (j = 0; j < size - shift_position; j++)
  {
    aux[j] = bits[j + shift_position];
  }

  int k = 0;
  int l = 0;
  for (k = size - shift_position; k < size; k++)
  {
    aux[k] = shifted_bits[l++];
  }

  return aux;
}

char *concatenate_substrings(char *left, char *rigth)
{
  char *aux = (char *)malloc(10 * sizeof(char));
  aux = strcpy(aux, left);
  return strcat(aux, rigth);
}

void key_generation(char *k, char **k1, char **k2)
{
  char *p10 = initial_permuttation(k);

  char *left = separation(p10, 0, 5);
  char *rigth = separation(p10, 5, 5);

  left = exec_rotation_left(left, 1, 5);
  rigth = exec_rotation_right(rigth, 1, 5);

  char *concatenated_strings_k1 = concatenate_substrings(left, rigth);
  *k1 = p8_permuttation(concatenated_strings_k1);

  char *k2_left = exec_rotation_left(left, 2, 5);
  char *k2_right = exec_rotation_right(rigth, 2, 5);
  char *k2_bits = concatenate_substrings(k2_left, k2_right);
  *k2 = permuttation(k2_bits, P8_array, 8);
}

char *exec_switch(char *bits, int size)
{
  char *sub_s_left = separation(bits, size / 2, size - 1);
  char *sub_s_right = separation(bits, 0, size / 2);
  return concatenate_substrings(sub_s_left, sub_s_right);
}

char *exec_xor(char *chain_of_bits, char *current_key, int size)
{
  int i;
  char *aux = (char *)malloc(size * sizeof(char));

  for (i = 0; i < size; i++)
  {
    if (chain_of_bits[i] == '1' && current_key[i] == '0')
    {
      aux[i] = '1';
    }

    else if (chain_of_bits[i] == '0' && current_key[i] == '1')
    {
      aux[i] = '1';
    }

    else
    {
      aux[i] = '0';
    }
  }

  return aux;
}

char *expand(char *bits)
{
  int i;
  int EP_array[] = {4, 1, 2, 3, 2, 3, 4, 1};
  char *aux = (char *)malloc(8 * sizeof(char));

  for (i = 0; i < 8; i++)
  {
    aux[i] = bits[EP_array[i] - 1];
  }

  return aux;
}

int convert_bin_to_int(char char_on_left, char char_on_right)
{
  if (char_on_left == '0' && char_on_right == '0')
  {
    return 0;
  }

  if (char_on_left == '0' && char_on_right == '1')
  {
    return 1;
  }

  if (char_on_left == '1' && char_on_right == '0')
  {
    return 2;
  }

  if (char_on_left == '1' && char_on_right == '1')
  {
    return 3;
  }
}

char *convert_int_to_bin(int position)
{
  char *new_bin = (char *)malloc(2 * sizeof(char));

  if (position == 0)
  {
    return strcat(new_bin, "000");
  }

  if (position == 1)
  {
    return strcat(new_bin, "01");
  }
  if (position == 2)
  {
    return strcat(new_bin, "10");
  }
  if (position == 3)
  {
    return strcat(new_bin, "11");
  }
  else
  {
    return new_bin;
  }
}

char *join(char *bits)
{
  int S0[4][4] = {
    1,
    0,
    3,
    2,
    3,
    2,
    1,
    0,
    0,
    2,
    1,
    3,
    3,
    1,
    3,
    2,
};

  int S1[4][4] = {
    1,
    1,
    2,
    3,
    2,
    0,
    1,
    3,
    3,
    0,
    1,
    0,
    2,
    1,
    0,
    3,
};

  char *sub_s_left = separation(bits, 0, 4);
  char *sub_s_right = separation(bits, 4, 4);

  int s0_row = convert_bin_to_int(sub_s_left[0], sub_s_left[3]);
  int s0_col = convert_bin_to_int(sub_s_left[1], sub_s_left[2]);

  int s1_row = convert_bin_to_int(sub_s_right[0], sub_s_right[3]);
  int s1_col = convert_bin_to_int(sub_s_right[1], sub_s_right[2]);

  char *aux_left_bin = convert_int_to_bin(S0[s0_row][s0_col]);
  char *aux_right_bin = convert_int_to_bin(S1[s1_row][s1_col]);

  return concatenate_substrings(aux_left_bin, aux_right_bin);
}

char *execute_function_f(char *bits, char *key)
{
  int P4_array[] = {2, 4, 3, 1};
  char *left = separation(bits, 0, 4);
  char *right = separation(bits, 4, 4);
  return concatenate_substrings(exec_xor(permuttation(join(exec_xor(expand(right), key, 8)), P4_array, 4), left, 4), right);
}

char *encripty_data(char *bin, char *support_key_1, char *support_key_2)
{
  char *switched_values = exec_switch(execute_function_f(permuttation(bin, initial_permutation, 8), support_key_1), 8);
  char *function_f_return = execute_function_f(switched_values, support_key_2);
  return permuttation(function_f_return, final_permutation, 8);
}

char *decripty_data(char *bin, char *support_key_1, char *support_key_2)
{
  char *switched_values = exec_switch(execute_function_f(permuttation(bin, initial_permutation, 8), support_key_2), 8);
  char *function_f_return = execute_function_f(switched_values, support_key_1);
  return permuttation(function_f_return, final_permutation, 8);
}

void run_desired_operation(char **answer,  char operation, char *bitSequence, char *key_1, char *key_2, int position){
  if (operation == 'C')
  {
    answer[position] = encripty_data(bitSequence, key_1, key_2);
  }
  else if (operation == 'D')
  {
    answer[position] = decripty_data(bitSequence, key_1, key_2);
  }
}

void print_answers(char **answer, int size)
{
  int i = 0;
  for (i; i < size; i++)
  {
    printf("%s\n", answer[i]);
  }
}

void init(int iterations)
{
  int i;
  char operation;

  char *key_string = (char *)malloc(10 * sizeof(char));
  char *bitSequence = (char *)malloc(8 * sizeof(char));
  char *key_1 = (char *)malloc(8 * sizeof(char));
  char *key_2 = (char *)malloc(8 * sizeof(char));
  char **answer = (char **)malloc(iterations * sizeof(char *));
  for (i = 0; i < iterations; i++)
    answer[i] = (char *)malloc(8 * sizeof(char));

  for (i = 0; i < iterations; i++)
  {
    scanf(" %c", &operation);
    scanf("%s", key_string);
    scanf("%s", bitSequence);

    key_generation(key_string, &key_1, &key_2);

    run_desired_operation(answer,  operation, bitSequence, key_1, key_2, i);
  }

  print_answers(answer, iterations);
}

int main(int argc, char **argv)
{
  int iterations;
  scanf("%d", &iterations);

  init(iterations);

  return 0;
}