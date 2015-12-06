// Check windows
#if _WIN32 || _WIN64
   #if _WIN64
     #define ENV64BIT
  #else
    #define ENV32BIT
  #endif
#endif

// Check GCC
#if __GNUC__
  #if __x86_64__ || __ppc64__
    #define ENV64BIT
  #else
    #define ENV32BIT
  #endif
#endif

// *******************************************************

#define JNU_GetLongFieldAsPtr(env,obj,id) (jlong_to_ptr((env)->GetLongField((obj),(id))))
#define JNU_SetLongFieldFromPtr(env,obj,id,val) (env)->SetLongField((obj),(id),ptr_to_jlong(val))

#if defined(ENV64BIT)
  /* 64-bit code here. */
  #define jlong_to_ptr(a) ((void*)(a))
  #define ptr_to_jlong(a) ((jlong)(a))
#elif defined (ENV32BIT)
  /* 32-bit code here. */
  // Double casting to avoid warning messages looking for casting of
  // smaller sizes into pointers //
  #define jlong_to_ptr(a) ((void*)(int)(a))
  #define ptr_to_jlong(a) ((jlong)(int)(a))
#else
  #error "Must define either ENV32BIT or ENV64BIT"
#endif