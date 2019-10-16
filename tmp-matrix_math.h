#include <stdlib.h>
#include <math.h>

/************************************
 *            type                  *
 ***********************************/
typedef float* vec;
typedef float* mat;
typedef float* tr;

/************************************
 *           vector                 *
 ***********************************/

vec vec_new();
void vec_copy(vec, vec);
void vec_add(vec, vec);
void vec_scale(vec, float);
void vec_dot(vec, vec);
void vec_cross(vec, vec);
void vec_normalize(vec);
void vec_delete(vec);

/************************************
 *           matrix                 *
 ***********************************/

mat mat_new();
void mat_copy(mat, mat);
void mat_add(mat, mat);
void mat_mult_mat(mat, mat);
void mat_print(mat);
void mat_delete(mat);

/************************************
 *           transform              *
 ***********************************/

tr tr_new();
void tr_trans(tr, float, float, float);
void tr_rotate(tr, float, float, float);
void tr_scale(tr, float, float, float);
void tr_delete(tr);
